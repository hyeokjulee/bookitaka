package com.bookitaka.NodeulProject.member.service;

import com.bookitaka.NodeulProject.member.exception.CustomException;
import com.bookitaka.NodeulProject.member.model.Member;
import com.bookitaka.NodeulProject.member.repository.MemberRepository;
import com.bookitaka.NodeulProject.member.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;
  private final AuthenticationManager authenticationManager;

  @Value("${access.token.cookie.name}")
  private String accessTokenCookieName;
  @Value("${refresh.token.cookie.name}")
  private String refreshTokenCookieName;

  /************************************************************************************************
   * Member Service
   ************************************************************************************************/

  @Transactional
  public void signin(String memberEmail, String memberPassword, HttpServletResponse httpServletResponse) {
    try {
      // 회원 로그인 인증
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(memberEmail, memberPassword));

      // access 토큰 발급 - HTTP-only 쿠키 저장
      String token = jwtTokenProvider.createToken(memberEmail, memberRepository.findByMemberEmail(memberEmail).getMemberRole());
      setCookie(httpServletResponse, token, accessTokenCookieName, false);

      // refresh 토큰 발급 - HTTP-only 쿠키 및 회원DB 저장
      String rtoken = jwtTokenProvider.createRefreshToken(memberEmail, memberRepository.findByMemberEmail(memberEmail).getMemberRole());
      Member member = memberRepository.findByMemberEmail(memberEmail);
      member.setMemberRtoken(rtoken);
      memberRepository.save(member);
      setCookie(httpServletResponse, rtoken, refreshTokenCookieName, false);

    } catch (AuthenticationException e) {
      throw new CustomException("Invalid email/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }

  public void signout(HttpServletResponse httpServletResponse) {
    setCookie(httpServletResponse, null, accessTokenCookieName,true);
    setCookie(httpServletResponse, null, refreshTokenCookieName,true);
  }

  public void signup(Member member) {
    if (!memberRepository.existsByMemberEmail(member.getMemberEmail())) {
      member.setMemberPassword(passwordEncoder.encode(member.getMemberPassword()));
      memberRepository.save(member);
    } else {
      throw new CustomException("Member email is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }

  public void delete(String memberEmail) {
    memberRepository.deleteByMemberEmail(memberEmail);
  }

  public Member search(String memberEmail) {
    Member member = memberRepository.findByMemberEmail(memberEmail);
    if (member == null) {
      throw new CustomException("The member doesn't exist", HttpStatus.NOT_FOUND);
    }
    return member;
  }

  public Member whoami(HttpServletRequest httpServletRequest) {
    return memberRepository.findByMemberEmail(jwtTokenProvider.getMemberEmail(jwtTokenProvider.resolveToken(httpServletRequest, accessTokenCookieName)));
  }

//  public Boolean checkToken(HttpServletRequest req) {
//    String token = jwtTokenProvider.resolveToken(req);
//    try {
//      if (token != null && jwtTokenProvider.validateToken(token)) {
//        return true;
//      }
//    } catch (CustomException ex) {
//      return false;
//    }
//    return false;
//  }

  /************************************************************************************************
   * JWT Service
   ************************************************************************************************/

  public boolean isValidToken(HttpServletRequest httpServletRequest) {
    String token = jwtTokenProvider.resolveToken(httpServletRequest, accessTokenCookieName);
    try {
      if (token != null && jwtTokenProvider.validateToken(token)) {
        return true;
      }
    } catch (CustomException ex) {
      return false;
    }
    return false;
  }

  public void refresh(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws CustomException {
    String rToken = jwtTokenProvider.resolveToken(httpServletRequest, refreshTokenCookieName);
    if (rToken != null && jwtTokenProvider.validateToken(rToken)) {
      String memberEmail = jwtTokenProvider.getMemberEmail(rToken);
      String token = jwtTokenProvider.createToken(memberEmail, memberRepository.findByMemberEmail(memberEmail).getMemberRole());
      setCookie(httpServletResponse, token, accessTokenCookieName, false);
    }
  }

  private void setCookie(HttpServletResponse httpServletResponse, String token, String tokenCookieName, boolean isSignout) {
    // 쿠키 생성 (혹은 제거를 위한 설정)
    Cookie cookie = new Cookie(tokenCookieName, token);
    cookie.setHttpOnly(true); // HTTP-only 속성 설정
    if (isSignout) {
      cookie.setMaxAge(0);
    }
    cookie.setPath("/"); // 쿠키의 유효 경로 설정 (루트 경로로 설정하면 모든 요청에서 사용 가능)

    // 응답에 쿠키 추가
    httpServletResponse.addCookie(cookie);
  }

}

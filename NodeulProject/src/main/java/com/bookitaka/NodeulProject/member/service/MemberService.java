package com.bookitaka.NodeulProject.member.service;

import com.bookitaka.NodeulProject.member.exception.CustomException;
import com.bookitaka.NodeulProject.member.model.Member;
import com.bookitaka.NodeulProject.member.repository.MemberRepository;
import com.bookitaka.NodeulProject.member.security.JwtTokenProvider;
import com.bookitaka.NodeulProject.member.security.Token;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;
  private final AuthenticationManager authenticationManager;

  /************************************************************************************************
   * Member Service
   ************************************************************************************************/

  @Transactional
  public void signin(String memberEmail, String memberPassword, HttpServletResponse response) {
    try {
      // 회원 로그인 인증
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(memberEmail, memberPassword));

      // access 토큰 발급 - HTTP-only 쿠키 저장
      String token = jwtTokenProvider.createToken(memberEmail, memberRepository.findByMemberEmail(memberEmail).getMemberRole());
      setCookie(response, token, Token.ACCESS_TOKEN, false);

      // refresh 토큰 발급 - HTTP-only 쿠키 및 회원DB 저장
      String rtoken = jwtTokenProvider.createRefreshToken(memberEmail, memberRepository.findByMemberEmail(memberEmail).getMemberRole());
      Member member = memberRepository.findByMemberEmail(memberEmail);
      member.setMemberRtoken(rtoken);
      memberRepository.save(member);
      setCookie(response, rtoken, Token.REFRESH_TOKEN, false);

    } catch (AuthenticationException e) {
      throw new CustomException("Invalid email/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }

  public void signout(HttpServletResponse response) {
    setCookie(response, null, Token.ACCESS_TOKEN,true);
    setCookie(response, null, Token.REFRESH_TOKEN,true);
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

  public Member whoami(HttpServletRequest request, String tokenCookieName) {
    return memberRepository.findByMemberEmail(jwtTokenProvider.getMemberEmail(jwtTokenProvider.resolveToken(request, tokenCookieName)));
  }

//  public Boolean checkToken(HttpServletRequest request) {
//    String token = jwtTokenProvider.resolveToken(request);
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

  public boolean isValidToken(HttpServletRequest request) {
    String token = jwtTokenProvider.resolveToken(request, Token.ACCESS_TOKEN);
    try {
      if (token != null && jwtTokenProvider.validateToken(token)) {
        return true;
      }
    } catch (CustomException ex) {
      return false;
    }
    return false;
  }

  public void refresh(HttpServletRequest request, HttpServletResponse response, Member member) throws CustomException, IOException {
    String req_rToken = jwtTokenProvider.resolveToken(request, Token.REFRESH_TOKEN);
    String db_rToken = member.getMemberRtoken();
    if (req_rToken != null && req_rToken.equals(db_rToken)) {
      String token = jwtTokenProvider.createToken(member.getMemberEmail(), member.getMemberRole());
      setCookie(response, token, Token.ACCESS_TOKEN, false);
    }
  }

  private void setCookie(HttpServletResponse response, String token, String tokenCookieName, boolean isSignout) {
    // 쿠키 생성 (혹은 제거를 위한 설정)
    Cookie cookie = new Cookie(tokenCookieName, token);
    cookie.setHttpOnly(true); // HTTP-only 속성 설정
    if (isSignout) {
      cookie.setMaxAge(0);
    }
    cookie.setPath("/"); // 쿠키의 유효 경로 설정 (루트 경로로 설정하면 모든 요청에서 사용 가능)

    // 응답에 쿠키 추가
    response.addCookie(cookie);
  }

}

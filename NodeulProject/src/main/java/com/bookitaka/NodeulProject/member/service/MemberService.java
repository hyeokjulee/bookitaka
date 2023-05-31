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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

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
  public Map<String, String> signin(String memberEmail, String memberPassword) {
    try {
      // 회원 로그인 인증
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(memberEmail, memberPassword));
      // access 토큰 발급 - HTTP-only 쿠키 저장
      String aToken = jwtTokenProvider.createToken(memberEmail, memberRepository.findByMemberEmail(memberEmail).getMemberRole());
      // refresh 토큰 발급 - HTTP-only 쿠키 및 회원DB 저장
      String rToken = jwtTokenProvider.createRefreshToken(memberEmail, memberRepository.findByMemberEmail(memberEmail).getMemberRole());
      Member member = memberRepository.findByMemberEmail(memberEmail);
      // Member DB에 저장
      member.setMemberRtoken(rToken);
      memberRepository.save(member);
      // 반환 Map객체에 두 토큰 저장
      Map<String, String> tokens = new HashMap<>();
      tokens.put(Token.ACCESS_TOKEN, aToken);
      tokens.put(Token.REFRESH_TOKEN, rToken);

      return tokens;

    } catch (AuthenticationException e) {
      throw new CustomException("Invalid email/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }

  public boolean signout(String memberEmail) {
    Member member = memberRepository.findByMemberEmail(memberEmail);
    if (member != null) {
      member.setMemberRtoken(null);
      memberRepository.save(member);
      return true;
    }
    return false;
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

  public Member whoami(Cookie[] cookies, String tokenCookieName) {
    return memberRepository.findByMemberEmail(jwtTokenProvider.getMemberEmail(jwtTokenProvider.resolveToken(cookies, tokenCookieName)));
  }



  /************************************************************************************************
   * JWT Service
   ************************************************************************************************/

  public boolean isValidToken(Cookie[] cookies) {
    String token = jwtTokenProvider.resolveToken(cookies, Token.ACCESS_TOKEN);
    try {
      if (token != null && jwtTokenProvider.validateToken(token)) {
        return true;
      }
    } catch (CustomException ex) {
      return false;
    }
    return false;
  }

  public String refresh(Cookie[] cookies, Member member) {
    String req_rToken = jwtTokenProvider.resolveToken(cookies, Token.REFRESH_TOKEN);
    String db_rToken = member.getMemberRtoken();
    if (req_rToken != null && req_rToken.equals(db_rToken)) {
      return jwtTokenProvider.createToken(member.getMemberEmail(), member.getMemberRole());
    }
    return null;
  }
}

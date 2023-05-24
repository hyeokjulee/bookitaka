package com.bookitaka.NodeulProject.member.service;

import com.bookitaka.NodeulProject.member.exception.CustomException;
import com.bookitaka.NodeulProject.member.model.Member;
import com.bookitaka.NodeulProject.member.repository.MemberRepository;
import com.bookitaka.NodeulProject.member.security.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;
  private final AuthenticationManager authenticationManager;

  public String signin(String memberEmail, String memberPassword) {
    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(memberEmail, memberPassword));
      return jwtTokenProvider.createToken(memberEmail, memberRepository.findByMemberEmail(memberEmail).getMemberRole());
    } catch (AuthenticationException e) {
      throw new CustomException("Invalid email/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }

  public String signup(Member member) {
    if (!memberRepository.existsByMemberEmail(member.getMemberEmail())) {
      member.setMemberPassword(passwordEncoder.encode(member.getMemberPassword()));
      memberRepository.save(member);
      return jwtTokenProvider.createToken(member.getMemberEmail(), member.getMemberRole());
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

  public Member whoami(HttpServletRequest req) {
    return memberRepository.findByMemberEmail(jwtTokenProvider.getMemberEmail(jwtTokenProvider.resolveToken(req)));
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

  public Boolean checkToken(String token) {
    try {
      if (token != null && jwtTokenProvider.validateToken(token)) {
        return true;
      }
    } catch (CustomException ex) {
      return false;
    }
    return false;
  }

  public Boolean isTokenExpired(HttpServletRequest req) {
    Cookie[] cookies = req.getCookies();
    String token = null;
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals("access_token")) {
          token = cookie.getValue();
          break;
        }
      }
    }
    log.info("isTokenExpired - token : {}", token);
    if (token != null) {
      Date expDate = jwtTokenProvider.getExpirationDate(token);
      Date now = new Date();
      log.info("isTokenExpired - expDate : {}", expDate);
      log.info("isTokenExpired - before : {}", expDate.before(now));
      return expDate.before(now);
    }
    return true;
  }

  public String refresh(String memberEmail) {
    return jwtTokenProvider.createToken(memberEmail, memberRepository.findByMemberEmail(memberEmail).getMemberRole());
  }

}

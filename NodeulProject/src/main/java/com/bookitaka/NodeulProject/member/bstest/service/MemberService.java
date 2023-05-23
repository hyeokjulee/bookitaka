package com.bookitaka.NodeulProject.member.bstest.service;

import com.bookitaka.NodeulProject.member.bstest.exception.CustomException;
import com.bookitaka.NodeulProject.member.bstest.model.Member;
import com.bookitaka.NodeulProject.member.bstest.repository.MemberRepository;
import com.bookitaka.NodeulProject.member.bstest.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

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
      throw new CustomException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }

  public String signup(Member member) {
    if (!memberRepository.existsByMemberEmail(member.getMemberEmail())) {
      member.setMemberPassword(passwordEncoder.encode(member.getMemberPassword()));
      memberRepository.save(member);
      return jwtTokenProvider.createToken(member.getMemberEmail(), member.getMemberRole());
    } else {
      throw new CustomException("Username is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }

  public void delete(String memberEmail) {
    memberRepository.deleteByMemberEmail(memberEmail);
  }

  public Member search(String memberEmail) {
    Member member = memberRepository.findByMemberEmail(memberEmail);
    if (member == null) {
      throw new CustomException("The user doesn't exist", HttpStatus.NOT_FOUND);
    }
    return member;
  }

  public Member whoami(HttpServletRequest req) {
    return memberRepository.findByMemberEmail(jwtTokenProvider.getMemberEmail(jwtTokenProvider.resolveToken(req)));
  }

  public String refresh(String username) {
    return jwtTokenProvider.createToken(username, memberRepository.findByMemberEmail(username).getMemberRole());
  }

}

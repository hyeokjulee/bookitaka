package com.bookitaka.NodeulProject.member.controller;

import com.bookitaka.NodeulProject.member.dto.*;
import com.bookitaka.NodeulProject.member.exception.CustomException;
import com.bookitaka.NodeulProject.member.model.Member;
import com.bookitaka.NodeulProject.member.security.Token;
import com.bookitaka.NodeulProject.member.service.MemberService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/test")
@Api(tags = "member")
@RequiredArgsConstructor
public class MemberTestController {

  private final MemberService memberService;
  private final ModelMapper modelMapper;

  // 회원가입
  @PostMapping("/signup")
  public void signup() {
    for (int i = 1; i <= 100; i++) {
      Member member = new Member();
      member.setMemberEmail("member" + i + "@naver.com");
      member.setMemberPassword("0000");
      member.setMemberName("member" + i);
      member.setMemberPhone("010 - " + i);
      member.setMemberGender("남성");
      member.setMemberBirthday("2023-06-01");
      memberService.signup(member);
    }
  }
}

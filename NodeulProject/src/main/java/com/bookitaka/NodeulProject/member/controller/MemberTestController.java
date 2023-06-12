package com.bookitaka.NodeulProject.member.controller;

import com.bookitaka.NodeulProject.faq.Faq;
import com.bookitaka.NodeulProject.faq.FaqRepository;
import com.bookitaka.NodeulProject.member.dto.*;
import com.bookitaka.NodeulProject.member.exception.CustomException;
import com.bookitaka.NodeulProject.member.model.Member;
import com.bookitaka.NodeulProject.member.security.Token;
import com.bookitaka.NodeulProject.member.service.MemberService;
import com.bookitaka.NodeulProject.notice.domain.entity.Notice;
import com.bookitaka.NodeulProject.notice.dto.NoticeDto;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/test")
@Api(tags = "member")
@RequiredArgsConstructor
public class MemberTestController {

  private final MemberService memberService;
  private final FaqRepository faqRepository;

  // 회원가입
  @GetMapping("/signup")
  public void signup() {
    for (int i = 1; i <= 100; i++) {
      Member member = new Member();
      member.setMemberEmail("member" + i + "@gmail.com");
      member.setMemberPassword("0000");
      member.setMemberName("member" + i);
      member.setMemberPhone("010 - " + i);
      member.setMemberGender("남성");
      member.setMemberBirthday("2023-06-01");
      memberService.signup(member);
    }
  }

  @PostMapping("/faq")
  public void faq() {

    for (int i = 1; i <= 50; i++) {
      Faq faq = new Faq();
      faq.setFaqAnswer("AAA"+i);
      faq.setFaqCategory("회원");
      faq.setFaqQuestion("QQQ"+i);
      faq.setFaqRegdate(new Date());
      faqRepository.save(faq);
    }
  }
}

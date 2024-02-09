package com.bookitaka.NodeulProject.member.controller;

import com.bookitaka.NodeulProject.faq.FaqRepository;
import com.bookitaka.NodeulProject.manual.ManualDto;
import com.bookitaka.NodeulProject.manual.ManualRepository;
import com.bookitaka.NodeulProject.member.model.Member;
import com.bookitaka.NodeulProject.member.security.Token;
import com.bookitaka.NodeulProject.member.service.MemberService;
import com.bookitaka.NodeulProject.notice.NoticeDto;
import com.bookitaka.NodeulProject.notice.NoticeRepository;
import com.bookitaka.NodeulProject.request.Request;
import com.bookitaka.NodeulProject.request.RequestRepository;
import com.bookitaka.NodeulProject.sheet.Sheet;
import com.bookitaka.NodeulProject.sheet.SheetRepository;
import com.bookitaka.NodeulProject.mysheet.Mysheet;
import com.bookitaka.NodeulProject.mysheet.MysheetRepository;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/test")
@Api(tags = "member")
@RequiredArgsConstructor
public class MemberTestController {

  private final MemberService memberService;
  private final FaqRepository faqRepository;
  private final ManualRepository manualRepository;
  private final NoticeRepository noticeRepository;
  private final RequestRepository requestRepository;
  private final SheetRepository sheetRepository;
  private final MysheetRepository mysheetRepository;

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

  @GetMapping("/manual")
  public void faq() {

    for (int i = 1; i <= 50; i++) {
      ManualDto m = new ManualDto();
      m.setManualContent("AAA" + i);
      m.setManualTitle("회원");
      manualRepository.save(m.toEntity());
    }
  }
  @GetMapping("/notice")
  public void notice() {

    for (int i = 1; i <= 50; i++) {
      NoticeDto m = new NoticeDto();
      m.setNoticeContent("AAA"+i);
      m.setNoticeTitle("회원");
      noticeRepository.save(m.toEntity());
    }
  }

  @GetMapping("/sheet")
  public void sheet() {

    for (int i = 1; i <= 50; i++) {
      Sheet sheet = new Sheet();
      sheet.setSheetBooktitle("title" + i);
      sheet.setSheetBookauthor("author" + i);
      sheet.setSheetBookpublisher("publisher" + i);
      sheet.setSheetPrice(10000);
      sheetRepository.createSheet(sheet);
    }
  }

  @GetMapping("/mysheet")
  public void mysheet(HttpServletRequest request) {

    for (int i = 1; i <= 50; i++) {
      Mysheet mysheet = new Mysheet();
      mysheet.setMember(memberService.whoami(request.getCookies(), Token.ACCESS_TOKEN));

      mysheetRepository.save(mysheet);
    }
  }

  @GetMapping("/request")
  public void request() {

    for (int i = 1; i <= 50; i++) {
      Request request = new Request();
      request.setRequestBookauthor("author" + i);
      request.setRequestBookpublisher("publisher" + i);
      request.setRequestBooktitle("Booktitle" + i);
      request.setRequestEmail("admin@gmail.com");
      request.setRequestContent("content" + i);
      request.setRequestName("name" + i);
      request.setRequestPhone("phone" + i);
      requestRepository.save(request);
    }
  }


}

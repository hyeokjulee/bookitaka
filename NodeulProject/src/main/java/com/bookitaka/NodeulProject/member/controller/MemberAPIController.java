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
import org.springframework.http.HttpStatus;
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
@RequestMapping("/member")
@Api(tags = "member")
@RequiredArgsConstructor
public class MemberAPIController {

  private final MemberService memberService;
  private final ModelMapper modelMapper;

  // 로그인
  @PostMapping("/signin")
  @ApiOperation(value = "${MemberController.signin}")
  @ApiResponses(value = {//
      @ApiResponse(code = 400, message = "Something went wrong"), //
      @ApiResponse(code = 422, message = "Invalid email/password supplied")})
  public String login(//
      @ApiParam("MemberEmail") @RequestParam("memberEmail") String memberEmail, //
      @ApiParam("MemberPassword") @RequestParam("memberPassword") String memberPassword,
      HttpServletResponse response) {
    log.info("================================Member : signin");
    Map<String, String> tokens = memberService.signin(memberEmail, memberPassword);
    setCookie(response, tokens.get(Token.ACCESS_TOKEN), Token.ACCESS_TOKEN, false);
    setCookie(response, tokens.get(Token.REFRESH_TOKEN), Token.REFRESH_TOKEN, false);
    return "Sign-in ok";
  }

  // 로그아웃
  @RequestMapping("/signout")
  @ApiOperation(value = "${MemberController.signout}")
  @ApiResponses(value = {//
      @ApiResponse(code = 403, message = "Access denied"), //
      @ApiResponse(code = 400, message = "Something went wrong"),
      @ApiResponse(code = 302, message = "redirect to login page")})
  public void logout(
      HttpServletRequest request,
      HttpServletResponse response,
      @RequestParam(defaultValue = "/members/login") String redirectUri) throws IOException {
    log.info("================================Member : signout");
    memberService.signout(request.getRemoteUser());
    setCookie(response, null, Token.ACCESS_TOKEN,true);
    setCookie(response, null, Token.REFRESH_TOKEN,true);
    response.sendRedirect(redirectUri);
  }

  // 회원가입
  @PostMapping("/signup")
  @ApiOperation(value = "${MemberController.signup}")
  @ApiResponses(value = {//
      @ApiResponse(code = 400, message = "Something went wrong"), //
      @ApiResponse(code = 402, message = "Agreement to the terms and conditions is required"),
      @ApiResponse(code = 422, message = "Member Email is already in use")})
  public ResponseEntity<?> signup(
          @ApiParam("Signup Member") @Validated @ModelAttribute MemberDataDTO memberDataDTO,
          @RequestParam(defaultValue = "false") Boolean agree1,
          @RequestParam(defaultValue = "false") Boolean agree2) {
    log.info("================================Member : signup");
    log.info("agree1 : {}", agree1);
    log.info("agree2 : {}", agree2);
    if (!agree1 || !agree2) {
      throw new CustomException("Agreement to the terms and conditions is required", HttpStatus.PAYMENT_REQUIRED);
    }
    memberService.signup(modelMapper.map(memberDataDTO, Member.class));
    return ResponseEntity.ok().body("Sign-up ok");
  }

  // 이미 존재하는 아이디 확인 (회원가입 시)
  @PostMapping("/checkid/{memberEmail}")
  @ApiOperation(value = "${MemberController.signupCheckDuplicateId}")
  @ApiResponses(value = {//
          @ApiResponse(code = 400, message = "Something went wrong"), //
          @ApiResponse(code = 422, message = "Member Email is already in use")})
  public ResponseEntity<String> signupCheckDuplicateId(@ApiParam("memberEmail") @PathVariable String memberEmail) {
    log.info("================================Member : signupCheckDuplicateId");
    try {
      memberService.search(memberEmail);
    } catch (CustomException e) {
      return ResponseEntity.ok().body("Email-available ok");
    }
    return ResponseEntity.unprocessableEntity().body("Member Email is already in use");
  }

  // 회원 삭제 (관리자)
  @DeleteMapping(value = "/admin/{memberEmail}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @ApiOperation(value = "${MemberController.delete}", authorizations = { @Authorization(value="apiKey") })
  @ApiResponses(value = {//
      @ApiResponse(code = 400, message = "Something went wrong"), //
      @ApiResponse(code = 403, message = "Access denied"), //
      @ApiResponse(code = 404, message = "The user doesn't exist")})
  public ResponseEntity<?> delete(@ApiParam("MemberEmail") @PathVariable String memberEmail) {
    log.info("================================Member : delete");
    memberService.delete(memberEmail);
    return ResponseEntity.ok().build();
  }

  // 회원 탈퇴 (회원)
  @DeleteMapping(value = "/{memberEmail}")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MEMBER')")
  @ApiOperation(value = "${MemberController.withdrawal}", authorizations = { @Authorization(value="apiKey") })
  @ApiResponses(value = {//
      @ApiResponse(code = 400, message = "Something went wrong"), //
      @ApiResponse(code = 403, message = "Access denied"), //
      @ApiResponse(code = 404, message = "The user doesn't exist")})
  public ResponseEntity<?> withdrawal(@ApiParam("MemberEmail") @PathVariable String memberEmail, HttpServletRequest request, HttpServletResponse response) {
    log.info("================================Member : withdrawal");
    if (memberEmail.equals(memberService.whoami(request.getCookies(), Token.ACCESS_TOKEN).getMemberEmail())) {
      setCookie(response, null, Token.ACCESS_TOKEN,true);
      setCookie(response, null, Token.REFRESH_TOKEN,true);
      memberService.delete(memberEmail);
    } else {
      return ResponseEntity.badRequest().build();
    }
    return ResponseEntity.ok().build();
  }

  // 회원 정보 수정 (회원)
  @PutMapping("/{memberEmail}")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MEMBER')")
  @ApiResponses(value = {//
          @ApiResponse(code = 400, message = "Something went wrong or permission mismatch"), //
          @ApiResponse(code = 403, message = "Access denied"), //
          @ApiResponse(code = 404, message = "The user doesn't exist"),
          @ApiResponse(code = 500, message = "Member edit failed")})
  public ResponseEntity<?> edit(@Validated @ModelAttribute MemberUpdateDTO memberUpdateDTO,
                                @PathVariable String memberEmail,
                                HttpServletRequest request) {
    log.info("================================Member : edit");
    String memberAuthEmail = memberService.whoami(request.getCookies(), Token.ACCESS_TOKEN).getMemberEmail();
    log.info("memberEmail : {}", memberEmail);
    log.info("memberAuthEmail : {}", memberAuthEmail);
    if (!memberEmail.equals(memberAuthEmail)) {
      return ResponseEntity.badRequest().body("permission mismatch");
    }
    Member member = memberService.search(memberEmail);
    modelMapper.map(memberUpdateDTO, member);
    if(memberService.modifyMember(member)) {
      // 수정 성공시
      return ResponseEntity.ok().build();
    } else {
      // 수정 실패 시
      return ResponseEntity.internalServerError().build();
    }
  }

  // 회원 정보 수정 (관리자)
  @PutMapping("/admin/{memberEmail}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @ApiResponses(value = {//
      @ApiResponse(code = 400, message = "Something went wrong"), //
      @ApiResponse(code = 403, message = "Access denied"), //
      @ApiResponse(code = 404, message = "The user doesn't exist"), //
      @ApiResponse(code = 500, message = "Member edit failed")})
  public ResponseEntity<?> editAdmin(@Validated @ModelAttribute MemberUpdateAdminDTO memberUpdateAdminDTO,
                                     @PathVariable String memberEmail) {
    log.info("================================ Member : editAdmin");
    Member member = memberService.search(memberEmail);
    modelMapper.map(memberUpdateAdminDTO, member);
    if(memberService.modifyMember(member)) {
      // 수정 성공시
      return ResponseEntity.ok().build();
    } else {
      // 수정 실패 시
      return ResponseEntity.internalServerError().build();
    }
  }

  // 비밀번호 변경
  @PutMapping("/changePw")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MEMBER')")
  public ResponseEntity<?> modifyPw(@Validated @ModelAttribute MemberChangePwDTO memberChangePwDTO,
                                         HttpServletRequest request,
                                         BindingResult bindingResult) {
    log.info("================================ Member : modifyPw");
    Member member = memberService.whoami(request.getCookies(), Token.ACCESS_TOKEN);
    int result = memberService.modifyPassword(member, memberChangePwDTO);

    if(bindingResult.hasErrors()) {
      return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
    }

    if (result==2) {
      bindingResult.rejectValue("newMemberPasswordCheck", "changePw.incorrectPw", "기존 비밀번호를 확인해주세요");
      log.info("===================== changePw bindingResult : {}",bindingResult.getAllErrors());

      return ResponseEntity.unprocessableEntity().body(bindingResult.getAllErrors());
    }

    if (result==1) {
      bindingResult.rejectValue("newMemberPasswordCheck", "changePw.samePw", "기존 비밀번호와 같은 비밀번호는 사용할 수 없습니다");
      log.info("===================== changePw bindingResult : {}",bindingResult.getAllErrors());

      return ResponseEntity.unprocessableEntity().body(bindingResult.getAllErrors());
    }

    return ResponseEntity.ok().build();
  }

  // 이메일 찾기
  @PostMapping("/findEmail")
  public ResponseEntity<?> findMemberEmail(@Validated @ModelAttribute MemberFindEmailDTO memberFindEmailDTO, BindingResult bindingResult) {
    log.info("================================ Member : findMemberEmail");
    List<String> members = memberService.getMemberEmail(memberFindEmailDTO);
    if(bindingResult.hasErrors()) {
      return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
    }
    if (members == null) {
      bindingResult.rejectValue("memberBirthday", "getMemberEmail.notFoundMember", "일치하는 회원이 없습니다");
      log.info("{}",bindingResult.getAllErrors());

      return ResponseEntity.unprocessableEntity().body(bindingResult.getAllErrors());
    }
    return ResponseEntity.ok().body(members);
  }

  // 비밀번호 찾기
  @PostMapping("/findPw")
  public ResponseEntity<?> findMemberPw(@Validated @ModelAttribute MemberFindPwDTO memberFindPwDTO, BindingResult bindingResult) {
    log.info("================================ Member : findMemberPw");
    if(bindingResult.hasErrors()) {
      return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
    }
    boolean result = memberService.getPwByEmailAndName(memberFindPwDTO.getMemberEmail(), memberFindPwDTO.getMemberName());
    if (!result) {
      bindingResult.rejectValue("memberName","getMemberPassword.notFoundMember","일치하는 회원이 없습니다");
      return ResponseEntity.unprocessableEntity().body(bindingResult.getAllErrors());
    }
    return ResponseEntity.ok().build();
  }

  // 회원 상세 보기 (관리자)
  @GetMapping(value = "/{memberEmail}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @ApiOperation(value = "${MemberController.search}", response = MemberResponseDTO.class, authorizations = { @Authorization(value="apiKey") })
  @ApiResponses(value = {//
      @ApiResponse(code = 400, message = "Something went wrong"), //
      @ApiResponse(code = 403, message = "Access denied"), //
      @ApiResponse(code = 404, message = "The user doesn't exist")})
  public MemberResponseDTO search(@ApiParam("MemberEmail") @PathVariable String memberEmail) {
    log.info("================================Member : search");
    return modelMapper.map(memberService.search(memberEmail), MemberResponseDTO.class);
  }

  // 내 정보 보기 (회원)
  @GetMapping(value = "/me")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MEMBER')")
  @ApiOperation(value = "${MemberController.me}", response = MemberResponseDTO.class, authorizations = { @Authorization(value="apiKey") })
  @ApiResponses(value = {//
      @ApiResponse(code = 400, message = "Something went wrong"), //
      @ApiResponse(code = 403, message = "Access denied")})
  public MemberResponseDTO whoami(HttpServletRequest request) {
    log.info("================================Member : whoami");
    return modelMapper.map(memberService.whoami(request.getCookies(), Token.ACCESS_TOKEN), MemberResponseDTO.class);
  }

  // 토큰 만료 여부 확인
  @GetMapping(value = "/token")
  @ApiOperation(value = "${MemberController.token}")
  @ApiResponses(value = {//
      @ApiResponse(code = 400, message = "Something went wrong"), //
      @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
  public String checkToken(HttpServletRequest request) {
    log.info("================================Member : checkToken");
    if (memberService.isValidToken(request.getCookies())) {
      return "Valid Token";
    } else {
      return "Invalid Token";
    }
  }

  // 토큰 재발급 (회원)
  @RequestMapping("/refresh")
  public ResponseEntity<String> refresh(
      HttpServletRequest request,
      HttpServletResponse response,
      @RequestParam(value = "redirectUri", defaultValue = "/") String redirectUri) throws IOException, ServletException {
    log.info("================================Member : refresh");
    log.info("redirectUri : {}", redirectUri);
    Member member = memberService.whoami(request.getCookies(), Token.REFRESH_TOKEN);
    String token = memberService.refresh(request.getCookies(), member);
    if (token != null) {
      setCookie(response, token, Token.ACCESS_TOKEN, false);
      if (redirectUri.equals("ajax")) {
        return ResponseEntity.status(HttpStatus.CREATED).build();
      }
      response.sendRedirect(redirectUri);
    }
    return ResponseEntity.badRequest().build();
  }

  private void setCookie(HttpServletResponse response, String token, String tokenCookieName, boolean isSignout) {
    // 쿠키 생성 (혹은 제거를 위한 설정)
    Cookie cookie = new Cookie(tokenCookieName, token);
    cookie.setHttpOnly(true); // HTTP-only 속성 설정
    if (isSignout) {
      cookie.setMaxAge(0);
    }
    cookie.setPath("/"); // 쿠키의 유효 경로 설정 (루트 경로로 설정하면 모든 요청에서 사용 가능)
    response.addCookie(cookie);
  }
}

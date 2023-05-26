package com.bookitaka.NodeulProject.member.controller;

import com.bookitaka.NodeulProject.member.dto.UserDataDTO;
import com.bookitaka.NodeulProject.member.dto.UserResponseDTO;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
    memberService.signin(memberEmail, memberPassword, response);
    return "sign-in ok";
  }

  // 로그아웃
  @GetMapping("/signout")
  @ApiOperation(value = "${MemberController.signout}")
  @ApiResponses(value = {//
      @ApiResponse(code = 400, message = "Something went wrong")})
  public void logout(
      HttpServletResponse response) throws IOException {
    log.info("================================Member : signout");
    memberService.signout(response);
    response.sendRedirect("/members/login");
  }

  // 회원가입
  @PostMapping("/signup")
  @ApiOperation(value = "${MemberController.signup}")
  @ApiResponses(value = {//
      @ApiResponse(code = 400, message = "Something went wrong"), //
      @ApiResponse(code = 403, message = "Access denied"), //
      @ApiResponse(code = 422, message = "Member Email is already in use")})
  public String signup(@ApiParam("Signup Member") @RequestBody UserDataDTO user) {
    log.info("================================Member : signup");
    memberService.signup(modelMapper.map(user, Member.class));
    return "Sign-up ok";
  }

  // 회원 삭제 (관리자)
  @DeleteMapping(value = "/{memberEmail}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @ApiOperation(value = "${MemberController.delete}", authorizations = { @Authorization(value="apiKey") })
  @ApiResponses(value = {//
      @ApiResponse(code = 400, message = "Something went wrong"), //
      @ApiResponse(code = 403, message = "Access denied"), //
      @ApiResponse(code = 404, message = "The user doesn't exist"), //
      @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
  public String delete(@ApiParam("MemberEmail") @PathVariable String memberEmail) {
    log.info("================================Member : delete");
    memberService.delete(memberEmail);
    return memberEmail;
  }

  // 회원 상세 보기 (관리자)
  @GetMapping(value = "/{memberEmail}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @ApiOperation(value = "${MemberController.search}", response = UserResponseDTO.class, authorizations = { @Authorization(value="apiKey") })
  @ApiResponses(value = {//
      @ApiResponse(code = 400, message = "Something went wrong"), //
      @ApiResponse(code = 403, message = "Access denied"), //
      @ApiResponse(code = 404, message = "The user doesn't exist"), //
      @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
  public UserResponseDTO search(@ApiParam("MemberEmail") @PathVariable String memberEmail) {
    log.info("================================Member : search");
    return modelMapper.map(memberService.search(memberEmail), UserResponseDTO.class);
  }

  // 내 정보 보기 (회원)
  @GetMapping(value = "/me")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MEMBER')")
  @ApiOperation(value = "${MemberController.me}", response = UserResponseDTO.class, authorizations = { @Authorization(value="apiKey") })
  @ApiResponses(value = {//
      @ApiResponse(code = 400, message = "Something went wrong"), //
      @ApiResponse(code = 403, message = "Access denied"), //
      @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
  public UserResponseDTO whoami(HttpServletRequest request) {
    log.info("================================Member : whoami");
    return modelMapper.map(memberService.whoami(request, Token.ACCESS_TOKEN), UserResponseDTO.class);
  }

  // 토큰 만료 여부 확인
  @GetMapping(value = "/token")
  @ApiOperation(value = "${MemberController.token}")
  @ApiResponses(value = {//
      @ApiResponse(code = 400, message = "Something went wrong"), //
      @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
  public String checkToken(HttpServletRequest request) {
    log.info("================================Member : checkToken");
    if (memberService.isValidToken(request)) {
      return "Valid Token";
    } else {
      return "Invalid Token";
    }
  }

  // 토큰 재발급 (회원)
  @GetMapping("/refresh")
//  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MEMBER')")
  public void refresh(HttpServletRequest request, HttpServletResponse response) throws IOException {
    log.info("================================Member : refresh");
    Member member = memberService.whoami(request, Token.REFRESH_TOKEN);
    memberService.refresh(request, response, member);
    response.sendRedirect((String) request.getAttribute("uri"));
  }

}

package com.bookitaka.NodeulProject.member.controller;

import com.bookitaka.NodeulProject.member.dto.UserDataDTO;
import com.bookitaka.NodeulProject.member.dto.UserResponseDTO;
import com.bookitaka.NodeulProject.member.exception.CustomException;
import com.bookitaka.NodeulProject.member.model.Member;
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

@Slf4j
@RestController
@RequestMapping("/member")
@Api(tags = "member")
@RequiredArgsConstructor
public class MemberAPIController {

  private final MemberService memberService;
  private final ModelMapper modelMapper;

  @PostMapping("/signin")
  @ApiOperation(value = "${MemberController.signin}")
  @ApiResponses(value = {//
      @ApiResponse(code = 400, message = "Something went wrong"), //
      @ApiResponse(code = 422, message = "Invalid email/password supplied")})
  public String login(//
      @ApiParam("MemberEmail") @RequestParam("memberEmail") String memberEmail, //
      @ApiParam("MemberPassword") @RequestParam("memberPassword") String memberPassword,
      HttpServletResponse response) {

    String token = memberService.signin(memberEmail, memberPassword);
    if (!memberService.checkToken(token)) {
      return "invalid";
    }

    // 쿠키 생성
    Cookie cookie = new Cookie("access_token", token);
    cookie.setHttpOnly(true); // HTTP-only 속성 설정
    cookie.setPath("/"); // 쿠키의 유효 경로 설정 (루트 경로로 설정하면 모든 요청에서 사용 가능)

    // 응답에 쿠키 추가
    response.addCookie(cookie);

    return "ok";
  }

  @PostMapping("/signout")
  @ApiOperation(value = "${MemberController.signin}")
  @ApiResponses(value = {//
      @ApiResponse(code = 400, message = "Something went wrong"), //
      @ApiResponse(code = 422, message = "Invalid email/password supplied")})
  public String logout(
      HttpServletResponse response) {

    // 쿠키 제거를 위한 설정
    Cookie cookie = new Cookie("access_token", "");
    cookie.setHttpOnly(true); // HTTP-only 속성 설정
    cookie.setMaxAge(0);
    cookie.setPath("/"); // 쿠키의 유효 경로 설정 (루트 경로로 설정하면 모든 요청에서 사용 가능)

    // 응답에 쿠키 추가
    response.addCookie(cookie);

    return "ok";
  }

  @PostMapping("/signup")
  @ApiOperation(value = "${MemberController.signup}")
  @ApiResponses(value = {//
      @ApiResponse(code = 400, message = "Something went wrong"), //
      @ApiResponse(code = 403, message = "Access denied"), //
      @ApiResponse(code = 422, message = "MemberEmail is already in use")})
  public String signup(@ApiParam("Signup Member") @RequestBody UserDataDTO user) {
    return memberService.signup(modelMapper.map(user, Member.class));
  }

  @DeleteMapping(value = "/{memberEmail}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @ApiOperation(value = "${MemberController.delete}", authorizations = { @Authorization(value="apiKey") })
  @ApiResponses(value = {//
      @ApiResponse(code = 400, message = "Something went wrong"), //
      @ApiResponse(code = 403, message = "Access denied"), //
      @ApiResponse(code = 404, message = "The user doesn't exist"), //
      @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
  public String delete(@ApiParam("MemberEmail") @PathVariable String memberEmail) {
    memberService.delete(memberEmail);
    return memberEmail;
  }

  @GetMapping(value = "/{memberEmail}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @ApiOperation(value = "${MemberController.search}", response = UserResponseDTO.class, authorizations = { @Authorization(value="apiKey") })
  @ApiResponses(value = {//
      @ApiResponse(code = 400, message = "Something went wrong"), //
      @ApiResponse(code = 403, message = "Access denied"), //
      @ApiResponse(code = 404, message = "The user doesn't exist"), //
      @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
  public UserResponseDTO search(@ApiParam("MemberEmail") @PathVariable String memberEmail) {
    return modelMapper.map(memberService.search(memberEmail), UserResponseDTO.class);
  }

  @GetMapping(value = "/me")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MEMBER')")
  @ApiOperation(value = "${MemberController.me}", response = UserResponseDTO.class, authorizations = { @Authorization(value="apiKey") })
  @ApiResponses(value = {//
      @ApiResponse(code = 400, message = "Something went wrong"), //
      @ApiResponse(code = 403, message = "Access denied"), //
      @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
  public UserResponseDTO whoami(HttpServletRequest req) {
    return modelMapper.map(memberService.whoami(req), UserResponseDTO.class);
  }

//  @GetMapping(value = "/token")
//  @ApiOperation(value = "${MemberController.token}")
//  @ApiResponses(value = {//
//          @ApiResponse(code = 400, message = "Something went wrong"), //
//          @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
//  public ResponseEntity<String> checkToken(HttpServletRequest req) {
//    if (memberService.checkToken(req)) {
//      String message = "Valid Token.";
//      return new ResponseEntity<>(message, HttpStatus.OK);
//    } else {
//      String message = "Invalid Token.";
//      return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
//    }
//  }

  @GetMapping(value = "/token")
  @ApiOperation(value = "${MemberController.token}")
  @ApiResponses(value = {//
          @ApiResponse(code = 400, message = "Something went wrong"), //
          @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
  public ResponseEntity<String> checkToken(HttpServletRequest req) {
    if (!memberService.isTokenExpired(req)) {
      String message = "Valid Token.";
      return new ResponseEntity<>(message, HttpStatus.OK);
    } else {
      String message = "Invalid Token.";
      return new ResponseEntity<>(message, HttpStatus.OK);
    }
  }

  @GetMapping("/refresh")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MEMBER')")
  public String refresh(HttpServletRequest req) {
    return memberService.refresh(req.getRemoteUser());
  }

}

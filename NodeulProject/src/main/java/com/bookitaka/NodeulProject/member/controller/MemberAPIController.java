package com.bookitaka.NodeulProject.member.controller;

import com.bookitaka.NodeulProject.member.dto.*;
import com.bookitaka.NodeulProject.member.model.Member;
import com.bookitaka.NodeulProject.member.security.Token;
import com.bookitaka.NodeulProject.member.service.MemberService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
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
    @GetMapping("/signout")
    @ApiOperation(value = "${MemberController.signout}")
    @ApiResponses(value = {//
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 400, message = "Something went wrong")})
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        log.info("================================Member : signout");
        memberService.signout(request.getRemoteUser());
        setCookie(response, null, Token.ACCESS_TOKEN, true);
        setCookie(response, null, Token.REFRESH_TOKEN, true);
        response.sendRedirect("/members/login");
    }

    // 회원가입
    @PostMapping("/signup")
    @ApiOperation(value = "${MemberController.signup}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 422, message = "Member Email is already in use")})
    public ResponseEntity<?> signup(@ApiParam("Signup Member") @Validated @ModelAttribute UserDataDTO user) {
        log.info("================================Member : signup");
        if (memberService.signup(modelMapper.map(user, Member.class))) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

    // 회원 삭제 (관리자)
    @DeleteMapping(value = "/{memberEmail}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "${MemberController.delete}", authorizations = {@Authorization(value = "apiKey")})
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

    @PutMapping("/{memberEmail}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MEMBER')")
    public ResponseEntity<?> edit(@Validated @ModelAttribute MemberUpdateDTO memberUpdateDTO,
                                  @PathVariable String memberEmail,
                                  HttpServletRequest request) {
        log.info("================================Member : edit");
        Member member = memberService.search(memberEmail);
        modelMapper.map(memberUpdateDTO, member);
        if (memberService.modifyMember(member)) {
            // 수정 성공시
            return ResponseEntity.ok().build();
        } else {
            // 수정 실패 시
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/changePw")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MEMBER')")
    public ResponseEntity<String> modifyPw(@ModelAttribute MemberChangePwDTO memberChangePwDTO,
                                           HttpServletRequest request
    ) {
        Member member = memberService.whoami(request.getCookies(), Token.ACCESS_TOKEN);
        if (memberService.modifyPassword(member, memberChangePwDTO)) {
            return ResponseEntity.ok("비밀번호 수정 성공");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("비밀번호 수정 실패");

        }
    }

    @PostMapping("/findEmail")
    public ResponseEntity<?> findMemberEmail(@Validated @ModelAttribute MemberFindEmailDTO memberFindEmailDTO, BindingResult bindingResult) {
        log.info("========================================================= findEmail");
        List<String> members = memberService.getMemberEmail(memberFindEmailDTO);
        log.info("========================================================= members : {}",members);
        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        if (members == null) {
            log.info("membersSize");
            bindingResult.rejectValue("memberBirthday", "getMemberEmail.notFoundMember", "일치하는 회원이 없습니다");
            log.info("{}",bindingResult.getAllErrors());

            return ResponseEntity.unprocessableEntity().body(bindingResult.getAllErrors());
        }
        log.info("OKOKOKOKOKOK");
        return ResponseEntity.ok().body(members);
    }


    @PostMapping("/findPw")
    public ResponseEntity<String> findMemberPw(@Validated @ModelAttribute MemberFindPwDTO memberFindPwDTO) {
        memberService.getPwByEmail(memberFindPwDTO);
        return ResponseEntity.ok("임시 비밀번호로 변경완료");
    }

    // 회원 상세 보기 (관리자)
    @GetMapping(value = "/{memberEmail}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "${MemberController.search}", response = UserResponseDTO.class, authorizations = {@Authorization(value = "apiKey")})
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
    @ApiOperation(value = "${MemberController.me}", response = UserResponseDTO.class, authorizations = {@Authorization(value = "apiKey")})
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    public UserResponseDTO whoami(HttpServletRequest request) {
        log.info("================================Member : whoami");
        return modelMapper.map(memberService.whoami(request.getCookies(), Token.ACCESS_TOKEN), UserResponseDTO.class);
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
    @GetMapping("/refresh")
//  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MEMBER')")
    public void refresh(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("================================Member : refresh");
        Member member = memberService.whoami(request.getCookies(), Token.REFRESH_TOKEN);
        String token = memberService.refresh(request.getCookies(), member);
        if (token != null) {
            setCookie(response, token, Token.ACCESS_TOKEN, false);
            response.sendRedirect((String) request.getAttribute("uri"));
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

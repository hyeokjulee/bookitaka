package com.bookitaka.NodeulProject.member.controller;

import com.bookitaka.NodeulProject.member.model.Member;
import com.bookitaka.NodeulProject.member.model.MemberRoles;
import com.bookitaka.NodeulProject.member.repository.MemberRepository;
import com.bookitaka.NodeulProject.member.security.JwtTokenProvider;
import com.bookitaka.NodeulProject.member.security.Token;
import com.bookitaka.NodeulProject.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.servlet.http.Cookie;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    MockMvc mockMvc;

    private final static String testEmail = "aaa@aaa.com";
    private final static String testPassword = "0000";
    private Member testMember = null;
    private String testToken = null;
    Cookie aTokenCookie = null;
    Cookie rTokenCookie = null;

    @BeforeEach
    void beforeTest() {
        testMember = new Member(null, testEmail, passwordEncoder.encode(testPassword), "tester",
                "010-0101-0101", "F", "2222-22-22", MemberRoles.ADMIN, null);
        memberRepository.save(testMember);
        testToken = jwtTokenProvider.createToken(testEmail, MemberRoles.ADMIN);

        aTokenCookie = new Cookie(Token.ACCESS_TOKEN, testToken);
        rTokenCookie = new Cookie(Token.REFRESH_TOKEN, testToken);
        aTokenCookie.setHttpOnly(true);
        rTokenCookie.setHttpOnly(true);
    }
    @AfterEach
    void afterTest() {
        memberRepository.delete(testMember);
    }

    @Test
    void login() throws Exception {
        // 테스트하고자 하는 URL 및 파라미터 설정
//        String url = "/members/edit";
//        String param1 = "value1";
//        String param2 = "value2";
//
//        // MockMvc를 사용하여 GET 요청 보내고 응답 결과를 검증
//        ResultActions result = mockMvc.perform(get(url)
//                .param("param1", param1)
//                .param("param2", param2));
//
//        // 응답 상태 코드 검증
//        result.andExpect(status().isOk());
//
//        // 응답 뷰 및 뷰 이름 검증
//        result.andExpect(view().name("your-view-name"));
//
//        // 뷰에서 전달하는 모델 속성 검증
//        result.andExpect(model().attributeExists("yourModelAttribute"));
//        result.andExpect(model().attribute("yourModelAttribute", "expectedValue"));
    }

    @Test
    @DisplayName("멤버 컨트롤러 - 회원 수정 뷰")
    void edit() throws Exception {
        // 테스트하고자 하는 URL 및 파라미터 설정
        String url = "/members/edit";

        // MockMvc를 사용하여 GET 요청 보내고 응답 결과를 검증
        ResultActions result = mockMvc.perform(get(url)
                .cookie(aTokenCookie)
                .cookie(rTokenCookie));

        // 응답 상태 코드 검증
        result.andExpect(status().isOk());

        // 응답 뷰 및 뷰 이름 검증
        result.andExpect(view().name("login/edit"));

        // 뷰에서 전달하는 모델 속성 검증
        result.andExpect(model().attributeExists("member"));
    }

    @Test
    void list() {
    }

    @Test
    void findId() {
    }

    @Test
    void findEmailResult() {
    }

    @Test
    void findPw() {
    }

    @Test
    void signup() {
    }

    @Test
    void changePw() {
    }
}
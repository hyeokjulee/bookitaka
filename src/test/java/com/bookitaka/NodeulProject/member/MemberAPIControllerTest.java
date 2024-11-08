package com.bookitaka.NodeulProject.member;

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
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class MemberAPIControllerTest {

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
        testToken = jwtTokenProvider.createToken(testEmail, MemberRoles.ADMIN);
        testMember = new Member(null, testEmail, passwordEncoder.encode(testPassword), "tester",
                "010-0101-0101", "F", "2222-22-22", MemberRoles.ADMIN, testToken, null);
        memberRepository.save(testMember);

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
    @DisplayName("Member API 컨트롤러 - 로그인")
    void login() throws Exception {
        //given
        //when
        ResultActions resultActions = mockMvc.perform(post("/member/signin")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("memberEmail", testEmail)
                .param("memberPassword", testPassword));
        Collection<String> tokens = resultActions.andReturn().getResponse().getHeaders("Set-Cookie");
        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string("Sign-in ok"));
        assertEquals(tokens.size(), 2);
    }

    @Test
    @DisplayName("Member API 컨트롤러 - 로그아웃")
    void logout() throws Exception {
        //given
        //when
        ResultActions resultActions = mockMvc.perform(get("/member/signout")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("memberEmail", testEmail)
                .param("memberPassword", testPassword));
        ResultActions resultActionsRedirect = mockMvc.perform(get(Objects.requireNonNull(resultActions.andReturn().getResponse().getRedirectedUrl())));
        Collection<String> tokens = resultActionsRedirect.andReturn().getResponse().getHeaders("Set-Cookie");
        //then
        resultActions
                .andExpect(status().is3xxRedirection());
        assertEquals(tokens.size(), 0);
    }

    @Test
    @Transactional
    @DisplayName("Member API 컨트롤러 - 회원가입")
    void signup() throws Exception {
        //given
        //when
        mockMvc.perform(post("/member/signup")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("memberEmail", "bbb@bbb.com")
                .param("memberPassword", "aaaa0000!!!!")
                .param("memberPasswordCheck", "aaaa0000!!!!")
                .param("memberName", "Signup")
                .param("memberPhone", "010-0101-0101")
                .param("memberGender", "남성")
                .param("memberBirthday", "2023-05-23")
                .param("agree1", "true")
                .param("agree2", "true"))
        //then
            .andExpect(status().isOk())
            .andExpect(content().string("Sign-up ok"));
    }

    @Test
    @DisplayName("Member API 컨트롤러 - 회원삭제")
    void deleteMember() throws Exception {
        //given
//        Cookie aTokenCookie = new Cookie(Token.ACCESS_TOKEN, testToken);
//        Cookie rTokenCookie = new Cookie(Token.REFRESH_TOKEN, testToken);
//        aTokenCookie.setHttpOnly(true);
//        rTokenCookie.setHttpOnly(true);
        MockHttpServletRequestBuilder requestBuilder = delete("/member/{memberEmail}", testEmail)
                .cookie(aTokenCookie)
                .cookie(rTokenCookie);
        //when
        ResultActions resultActions = mockMvc.perform(requestBuilder);
        //then
        resultActions
                .andExpect(status().isOk());
//                .andExpect(content().string(testEmail));
    }

    @Test
    @DisplayName("Member API 컨트롤러 - 회원 정보 가져오기")
    void search() throws Exception {
        //given
//        Cookie aTokenCookie = new Cookie(Token.ACCESS_TOKEN, testToken);
//        Cookie rTokenCookie = new Cookie(Token.REFRESH_TOKEN, testToken);
//        aTokenCookie.setHttpOnly(true);
//        rTokenCookie.setHttpOnly(true);
        MockHttpServletRequestBuilder requestBuilder = get("/member/{memberEmail}", testEmail)
                .cookie(aTokenCookie)
                .cookie(rTokenCookie);
        //when
        ResultActions resultActions = mockMvc.perform(requestBuilder);
        //then
//        String resultJson = "{\"memberEmail\":\"aaa@aaa.com\"," +
//                "\"memberName\":\"tester\"," +
//                "\"memberPhone\":\"010-0101-0101\"," +
//                "\"memberGender\":\"F\"," +
//                "\"memberBirthday\":\"2222-22-22\"," +
//                "\"memberRole\":\"ROLE_ADMIN\"}";
        resultActions
                .andExpect(status().isOk());
//                .andExpect(content().json(resultJson));
    }

    @Test
    @DisplayName("Member API 컨트롤러 - 내 정보 가져오기")
    void me() throws Exception {
        //given
//        Cookie aTokenCookie = new Cookie(Token.ACCESS_TOKEN, testToken);
//        Cookie rTokenCookie = new Cookie(Token.REFRESH_TOKEN, testToken);
//        aTokenCookie.setHttpOnly(true);
//        rTokenCookie.setHttpOnly(true);
        MockHttpServletRequestBuilder requestBuilder = get("/member/me")
                .cookie(aTokenCookie)
                .cookie(rTokenCookie);
        //when
        ResultActions resultActions = mockMvc.perform(requestBuilder);
        //then
        String resultJson = "{\"memberEmail\":\"aaa@aaa.com\"," +
                "\"memberName\":\"tester\"," +
                "\"memberPhone\":\"010-0101-0101\"," +
                "\"memberGender\":\"F\"," +
                "\"memberBirthday\":\"2222-22-22\"," +
                "\"memberRole\":\"ROLE_ADMIN\"}";
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().json(resultJson));
    }

    @Test
    @DisplayName("Member API 컨트롤러 - 회원 수정")
    void edit() throws Exception {
        //given
//        Cookie aTokenCookie = new Cookie(Token.ACCESS_TOKEN, testToken);
//        Cookie rTokenCookie = new Cookie(Token.REFRESH_TOKEN, testToken);
//        aTokenCookie.setHttpOnly(true);
//        rTokenCookie.setHttpOnly(true);
        MockHttpServletRequestBuilder requestBuilder1 = put("/member/{memberEmail}", testEmail)
                .cookie(aTokenCookie)
                .cookie(rTokenCookie)
                .contentType("application/x-www-form-urlencoded")
                .param("memberName", "aaa")
                .param("memberPhone", "010-0101-0101")
                .param("memberGender", "여성")
                .param("memberBirthday", "2011-01-02");
        MockHttpServletRequestBuilder requestBuilder2 = put("/member/{memberEmail}", testEmail)
                .cookie(aTokenCookie)
                .cookie(rTokenCookie)
                .contentType("application/x-www-form-urlencoded")
                .param("memberName", " ")
                .param("memberPhone", "111")
                .param("memberGender", "222");
        MockHttpServletRequestBuilder requestBuilder3 = put("/member/{memberEmail}", testEmail)
                .cookie(aTokenCookie)
                .cookie(rTokenCookie);
        //when
        ResultActions resultActions1 = mockMvc.perform(requestBuilder1);
        ResultActions resultActions2 = mockMvc.perform(requestBuilder2);
        ResultActions resultActions3 = mockMvc.perform(requestBuilder3);
        //then
        resultActions1
                .andExpect(status().isOk());
        resultActions2
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Member API 컨트롤러 - 토큰 유효성 확인 (로그인 확인)")
    void checkToken() throws Exception {
        //given
//        Cookie aTokenCookie = new Cookie(Token.ACCESS_TOKEN, testToken);
//        Cookie rTokenCookie = new Cookie(Token.REFRESH_TOKEN, testToken);
//        aTokenCookie.setHttpOnly(true);
//        rTokenCookie.setHttpOnly(true);
        MockHttpServletRequestBuilder requestBuilder = get("/member/token")
                .cookie(aTokenCookie)
                .cookie(rTokenCookie);
        //when
        ResultActions resultActions = mockMvc.perform(requestBuilder);
        ResultActions resultActionsInvalid = mockMvc.perform(get("/member/token"));
        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string("Valid Token"));
        resultActionsInvalid
                .andExpect(status().isOk())
                .andExpect(content().string("Invalid Token"));
    }

    @Test
    @DisplayName("Member API 컨트롤러 - 토큰 재발급")
    void refresh() throws Exception {
        //given
//        Cookie aTokenCookie = new Cookie(Token.ACCESS_TOKEN, testToken);
//        Cookie rTokenCookie = new Cookie(Token.REFRESH_TOKEN, testToken);
//        aTokenCookie.setHttpOnly(true);
//        rTokenCookie.setHttpOnly(true);
        MockHttpServletRequestBuilder requestBuilder = get("/member/refresh")
                .cookie(aTokenCookie)
                .cookie(rTokenCookie);
        //when
        ResultActions resultActions = mockMvc.perform(requestBuilder);
        ResultActions resultActionsRedirect =
                mockMvc.perform(get(Objects.requireNonNull(resultActions.andReturn().getResponse().getRedirectedUrl())));
        Collection<String> tokens = resultActionsRedirect
                .andReturn()
                .getResponse()
                .getHeaders("Set-Cookie");
        //then
        resultActions
                .andExpect(status()
                        .is3xxRedirection());
    }

    @Test
    @Transactional
    @DisplayName("Member API 컨트롤러 - 회원가입 - 이메일 중복 체크")
    void emailCheck() throws Exception {
        //given
        //when
        mockMvc.perform(post("/member/signup")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("memberEmail", "aaa@aaa.com")
                        .param("memberPassword", "aaaa0000!!!!")
                        .param("memberPasswordCheck", "aaaa0000!!!!")
                        .param("memberName", "Signup")
                        .param("memberPhone", "010-0101-0101")
                        .param("memberGender", "남성")
                        .param("memberBirthday", "2023-05-23")
                        .param("agree1", "true")
                        .param("agree2", "true"))
                //then
                .andExpect(status().isUnprocessableEntity());
    }
}
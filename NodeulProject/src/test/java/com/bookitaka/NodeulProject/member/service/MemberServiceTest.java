package com.bookitaka.NodeulProject.member.service;

import com.bookitaka.NodeulProject.member.model.Member;
import com.bookitaka.NodeulProject.member.model.MemberRoles;
import com.bookitaka.NodeulProject.member.repository.MemberRepository;
import com.bookitaka.NodeulProject.member.security.JwtTokenProvider;
import com.bookitaka.NodeulProject.member.security.Token;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    private final static String testEmail = "aaa@aaa.com";
    private final static String testPassword = "0000";

    @BeforeEach
    void beforeTest() {
        Member member = new Member(null, testEmail, testPassword, "tester", "010-0101-0101", "F", new Date(), MemberRoles.ADMIN, null);
        memberService.signup(member);
    }

    @AfterEach
    void afterTest() {
        memberRepository.deleteByMemberEmail(testEmail);
    }

    @Test
    @DisplayName("회원가입")
    @Transactional
    public void signup() {
        //given
        String email = "test@test.com";
        Member member = new Member(null, email, "0000", "tester", "010-0101-0101", "F", new Date(), MemberRoles.MEMBER, null);
        //when
        memberService.signup(member);
        //then
        assertEquals(member.getMemberEmail(), memberRepository.findByMemberEmail(email).getMemberEmail());
    }

    @Test
    @DisplayName("로그인")
    public void signin() {
        //given

        //when
        Map<String, String> tokens = memberService.signin(testEmail, testPassword);
        //then
        String aToken = tokens.get(Token.ACCESS_TOKEN);
        String rToken = tokens.get(Token.REFRESH_TOKEN);
        assertNotNull(aToken);
        assertNotNull(rToken);
    }

    @Test
    @DisplayName("로그아웃")
    public void signout() throws Exception {
        //given

        //when
        boolean result = memberService.signout(testEmail);
        //then
        assertTrue(result);
    }

    @Test
    @DisplayName("회원삭제")
    public void delete() {
        //given

        //when
        memberService.delete(testEmail);
        //then
        Member member = memberRepository.findByMemberEmail(testEmail);
        assertNull(member);
    }

    @Test
    @DisplayName("토큰으로 회원객체 가져오기")
    public void whoami() {
        //given
        String aToken = jwtTokenProvider.createToken(testEmail, MemberRoles.ADMIN);
        String rToken = jwtTokenProvider.createRefreshToken(testEmail, MemberRoles.ADMIN);
        Cookie[] cookies = new Cookie[] {new Cookie(Token.ACCESS_TOKEN, aToken), new Cookie(Token.REFRESH_TOKEN, rToken)};
        //when
        Member member = memberService.whoami(cookies, Token.ACCESS_TOKEN);
        //then
        assertEquals(member.getMemberEmail(), testEmail);
    }

    @Test
    @DisplayName("토큰 유효성 확인")
    public void isValidToken() {
        //given
        String aToken = jwtTokenProvider.createToken(testEmail, MemberRoles.ADMIN);
        String rToken = jwtTokenProvider.createRefreshToken(testEmail, MemberRoles.ADMIN);
        Cookie[] cookies = new Cookie[] {new Cookie(Token.ACCESS_TOKEN, aToken), new Cookie(Token.REFRESH_TOKEN, rToken)};
        //when
        boolean valid = memberService.isValidToken(cookies);
        boolean invalid1 = memberService.isValidToken(new Cookie[] {new Cookie(Token.ACCESS_TOKEN, "")});
        boolean invalid2 = memberService.isValidToken(new Cookie[] {new Cookie(Token.ACCESS_TOKEN, "abc")});
        boolean invalid3 = memberService.isValidToken(null);
        //then
        assertTrue(valid);
        assertFalse(invalid1);
        assertFalse(invalid2);
        assertFalse(invalid3);
    }

    @Test
    @DisplayName("리프레시 토큰으로 액세스 토큰 재발급")
    public void refresh() {
        //given
        String aToken = jwtTokenProvider.createToken(testEmail, MemberRoles.ADMIN);
        String rToken = jwtTokenProvider.createRefreshToken(testEmail, MemberRoles.ADMIN);
        Cookie[] cookies = new Cookie[] {new Cookie(Token.ACCESS_TOKEN, aToken), new Cookie(Token.REFRESH_TOKEN, rToken)};
        Member hasNotTokenMember = memberRepository.findByMemberEmail(testEmail);
        Member hasTokenMember = memberRepository.findByMemberEmail(testEmail);
        hasTokenMember.setMemberRtoken(rToken);
        //when
        String validToken = memberService.refresh(cookies, hasTokenMember);
        String nullToken = memberService.refresh(cookies, hasNotTokenMember);
        //then
        assertNull(nullToken);
        assertNotNull(validToken);
    }
}
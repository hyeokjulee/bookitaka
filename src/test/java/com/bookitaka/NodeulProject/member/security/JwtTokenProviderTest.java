package com.bookitaka.NodeulProject.member.security;

import com.bookitaka.NodeulProject.member.model.Member;
import com.bookitaka.NodeulProject.member.model.MemberRoles;
import com.bookitaka.NodeulProject.member.repository.MemberRepository;
import com.bookitaka.NodeulProject.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.servlet.http.Cookie;
import javax.validation.constraints.AssertTrue;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class JwtTokenProviderTest {

    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    MemberRepository memberRepository;

    private final static String testEmail = "aaa@aaa.com";
    private final static String testPassword = "0000";
    private Member testMember = null;
    private String testToken = null;

    @BeforeEach
    void beforeTest() {
        testMember = new Member(null, testEmail, testPassword, "tester", "010-0101-0101", "F", "2222-22-22", MemberRoles.ADMIN, null);
        memberRepository.save(testMember);
        testToken = jwtTokenProvider.createToken(testEmail, MemberRoles.ADMIN);
    }
    @AfterEach
    void afterTest() {
        memberRepository.delete(testMember);
    }

    @Test
    @DisplayName("액세스 토큰 발급 및 유효성 검사")
    void createAndValidateToken() {
        //given
        //when
        String token = jwtTokenProvider.createToken(testMember.getMemberEmail(), testMember.getMemberRole());
        //then
        assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    @DisplayName("리프레시 토큰 발급 및 유효성 검사")
    void createAndValidateRefreshToken() {
        //given
        //when
        String token = jwtTokenProvider.createRefreshToken(testMember.getMemberEmail(), testMember.getMemberRole());
        //then
        assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    @DisplayName("쿠키에서 토큰 가져오기")
    void resolveToken() {
        //given
        String aToken = jwtTokenProvider.createToken(testEmail, MemberRoles.ADMIN);
        String rToken = jwtTokenProvider.createRefreshToken(testEmail, MemberRoles.ADMIN);
        Cookie[] cookies = new Cookie[] {new Cookie(Token.ACCESS_TOKEN, aToken), new Cookie(Token.REFRESH_TOKEN, rToken)};
        //when
        String resolvedAccessToken = jwtTokenProvider.resolveToken(cookies, Token.ACCESS_TOKEN);
        String resolvedRefreshToken = jwtTokenProvider.resolveToken(cookies, Token.REFRESH_TOKEN);
        //then
        assertTrue(jwtTokenProvider.validateToken(resolvedAccessToken));
        assertTrue(jwtTokenProvider.validateToken(resolvedRefreshToken));
    }

    @Test
    @DisplayName("토큰에서 권한 정보 객체 가져오기")
    void getAuthentication() {
        //given
        //when
        String email = jwtTokenProvider.getAuthentication(testToken).getName();
        //then
        assertEquals(email, testEmail);
    }

    @Test
    @DisplayName("토큰에서 이메일 가져오기")
    void getMemberEmail() {
        //given
        //when
        String email = jwtTokenProvider.getMemberEmail(testToken);
        //then
        assertEquals(email, testEmail);
    }

    @Test
    @DisplayName("토큰 만료시간 가져오기")
    void getExpirationDate() {
        //given
        //when
        Date expDate = jwtTokenProvider.getExpirationDate(testToken);
        //then
        assertNotNull(expDate);
    }

    @Test
    @DisplayName("토큰 유효성 검사")
    void validateToken() {
        //given
        //when
        //then
        assertTrue(jwtTokenProvider.validateToken(testToken));
    }

}

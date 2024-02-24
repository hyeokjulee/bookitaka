package com.bookitaka.NodeulProject.member;

import com.bookitaka.NodeulProject.member.model.Member;
import com.bookitaka.NodeulProject.member.model.MemberRoles;
import com.bookitaka.NodeulProject.member.repository.MemberRepository;
import com.bookitaka.NodeulProject.member.security.JwtTokenProvider;
import com.bookitaka.NodeulProject.member.security.Token;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {

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
                "010-0101-0101", "F", "2222-22-22", MemberRoles.ADMIN, Token.REFRESH_TOKEN, null);
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
        String url = "/members/login";

        // MockMvc를 사용하여 GET 요청 보내고 응답 결과를 검증
        ResultActions result = mockMvc.perform(get(url));

        // 응답 상태 코드 검증
        result.andExpect(status().isOk());

        // 응답 뷰 및 뷰 이름 검증
        result.andExpect(view().name("member/login/login"));
    }

    @Test
    void signup() throws Exception {
        // 테스트하고자 하는 URL 및 파라미터 설정
        String url = "/members/signup";

        // MockMvc를 사용하여 GET 요청 보내고 응답 결과를 검증
        ResultActions result = mockMvc.perform(get(url));

        // 응답 상태 코드 검증
        result.andExpect(status().isOk());

        // 응답 뷰 및 뷰 이름 검증
        result.andExpect(view().name("member/login/signup"));

        // MockMvc를 사용하여 GET 요청 보내고 응답 결과를 검증
        result = mockMvc.perform(get(url)
                .cookie(aTokenCookie)
                .cookie(rTokenCookie));
        // 응답 상태 코드 검증
        result.andExpect(status().is3xxRedirection());
        // 응답 뷰 및 뷰 이름 검증
        result.andExpect(view().name("redirect:/"));
    }

    @Test
    @DisplayName("멤버 컨트롤러 - 회원 수정 뷰")
    void edit() throws Exception {
        // 테스트하고자 하는 URL 및 파라미터 설정
        String url = "/members/info";

        // MockMvc를 사용하여 GET 요청 보내고 응답 결과를 검증
        ResultActions result = mockMvc.perform(get(url)
                .cookie(aTokenCookie)
                .cookie(rTokenCookie));

        // 응답 상태 코드 검증
        result.andExpect(status().isOk());

        // 응답 뷰 및 뷰 이름 검증
        result.andExpect(view().name("member/my-info"));

        // 뷰에서 전달하는 모델 속성 검증
        result.andExpect(model().attributeExists("member"));
    }

    @Test
    void list() throws Exception {
        // 테스트하고자 하는 URL 및 파라미터 설정
        String url = "/members/list";

        // MockMvc를 사용하여 GET 요청 보내고 응답 결과를 검증
        ResultActions result = mockMvc.perform(get(url)
                .cookie(aTokenCookie)
                .cookie(rTokenCookie));

        // 응답 상태 코드 검증
        result.andExpect(status().isOk());

        // 응답 뷰 및 뷰 이름 검증
        result.andExpect(view().name("member/admin/list"));

        // 뷰에서 전달하는 모델 속성 검증
        result.andExpect(model().attributeExists("members"));
    }

    @Test
    void findEmail() throws Exception {
        // 테스트하고자 하는 URL 및 파라미터 설정
        String url = "/members/find-email";

        // MockMvc를 사용하여 GET 요청 보내고 응답 결과를 검증
        ResultActions result = mockMvc.perform(get(url));

        // 응답 상태 코드 검증
        result.andExpect(status().isOk());

        // 응답 뷰 및 뷰 이름 검증
        result.andExpect(view().name("member/login/findEmail"));

        // MockMvc를 사용하여 GET 요청 보내고 응답 결과를 검증
        result = mockMvc.perform(get(url)
                .cookie(aTokenCookie)
                .cookie(rTokenCookie));
        // 응답 상태 코드 검증
        result.andExpect(status().is3xxRedirection());
        // 응답 뷰 및 뷰 이름 검증
        result.andExpect(view().name("redirect:/"));
    }

//    @Test
//    void findEmailResult() throws Exception {
//    }

    @Test
    void findPw() throws Exception {
        // 테스트하고자 하는 URL 및 파라미터 설정
        String url = "/members/find-pw";

        // MockMvc를 사용하여 GET 요청 보내고 응답 결과를 검증
        ResultActions result = mockMvc.perform(get(url));

        // 응답 상태 코드 검증
        result.andExpect(status().isOk());

        // 응답 뷰 및 뷰 이름 검증
        result.andExpect(view().name("member/login/findPw"));

        // MockMvc를 사용하여 GET 요청 보내고 응답 결과를 검증
        result = mockMvc.perform(get(url)
                .cookie(aTokenCookie)
                .cookie(rTokenCookie));
        // 응답 상태 코드 검증
        result.andExpect(status().is3xxRedirection());
        // 응답 뷰 및 뷰 이름 검증
        result.andExpect(view().name("redirect:/"));
    }

    @Test
    void changePw() throws Exception {
        // 테스트하고자 하는 URL 및 파라미터 설정
        String url = "/members/changePw";

        // MockMvc를 사용하여 GET 요청 보내고 응답 결과를 검증
        ResultActions result = mockMvc.perform(get(url)
                .cookie(aTokenCookie)
                .cookie(rTokenCookie));

        // 응답 상태 코드 검증
        result.andExpect(status().isOk());

        // 응답 뷰 및 뷰 이름 검증
        result.andExpect(view().name("member/changePw"));
    }
}
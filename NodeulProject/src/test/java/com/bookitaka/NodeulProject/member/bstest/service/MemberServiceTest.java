package com.bookitaka.NodeulProject.member.bstest.service;

import com.bookitaka.NodeulProject.member.model.Member;
import com.bookitaka.NodeulProject.member.repository.MemberRepository;
import com.bookitaka.NodeulProject.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@Slf4j
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    private MockMvc mockMvc;



    @Test
    @Rollback(false)
    public void 회원가입_로그인_회원탈퇴() throws Exception{
        /* 회원가입 */
        //given
        Member member = new Member();
        member.setMemberEmail("asd@asd.com");
        member.setMemberPassword("0000");
        member.setMemberName("asd");
        member.setMemberPhone("0101");
        member.setMemberGender("male");
        member.setMemberRole("asd");

        //when
        String signupToken = memberService.signup(member);
        //then
//        assertEquals(member, memberRepository.findOne(savedId));

        /* 로그인 */
        //given
        String memberEmail = "asd@asd.com";
        String memberPassword = "0000";
        // Perform HTTP request
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/member/signin")
                .accept(MediaType.APPLICATION_JSON));
        HttpServletResponse response = resultActions.andReturn().getResponse();
        //when
        memberService.signin(memberEmail, memberPassword, response);
        //then
//        assertEquals(member, memberRepository.findOne(savedId));

        /* 회원탈퇴 */
        memberService.delete(memberEmail);
    }

    @Test
    @Rollback(false)
    public void 회원가입() throws Exception{
        /* 회원가입 */
        //given
        Member member = new Member();
        member.setMemberEmail("qwer@qwer.com");
        member.setMemberPassword("0000");
        member.setMemberName("John");
        member.setMemberPhone("0101");
        member.setMemberGender("Male");
        member.setMemberRole("MEMBER_ROLE");

        //when
        String signupToken = memberService.signup(member);
        log.info("signupToken: {}", signupToken);
        //then
//        assertEquals(member, memberRepository.findOne(savedId));
    }

//    @Test(expected = IllegalStateException.class)
//    public void 중복_회원_예외() throws Exception{
//        //given
//        Member member1 = new Member();
//        member1.setName("kim1");
//
//        Member member2 = new Member();
//        member2.setName("kim1");
//
//        //when
//        memberService.join(member1);
//        memberService.join(member2); //예외가 발생해야 한다.
//
//        //then
//
//    }

}
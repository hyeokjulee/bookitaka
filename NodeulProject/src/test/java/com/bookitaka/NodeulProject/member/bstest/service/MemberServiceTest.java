package com.bookitaka.NodeulProject.member.bstest.service;

import com.bookitaka.NodeulProject.member.dto.MemberChangePwDTO;
import com.bookitaka.NodeulProject.member.dto.MemberUpdateDTO;
import com.bookitaka.NodeulProject.member.dto.UserResponseDTO;
import com.bookitaka.NodeulProject.member.model.Member;
import com.bookitaka.NodeulProject.member.repository.MemberRepository;
import com.bookitaka.NodeulProject.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@Transactional
@Slf4j
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;
    UserResponseDTO userResponseDTO;



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
        log.info("signupToken: {}", signupToken);
        //then
//        assertEquals(member, memberRepository.findOne(savedId));

        /* 로그인 */
        //given
        String memberEmail = "asd@asd.com";
        String memberPassword = "0000";

        //when
        String signinToken = memberService.signin(memberEmail, memberPassword);
        log.info("signinToken: {}", signinToken);
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
        Date date = new Date();
        String dateString = "20120101";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        date = dateFormat.parse(dateString);
        Member member = new Member();
        member.setMemberEmail("zz@test.com");
        member.setMemberPassword("0000");
        member.setMemberName("John");
        member.setMemberPhone("0101");
        member.setMemberGender("Male");
        member.setMemberRole("MEMBER_ROLE");
        member.setMemberBirthday(date);

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
    @Test
    @Rollback(false)
    public void 수정() throws Exception {
        Date date = new Date();
        String dateString = "20120101";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        try {
            date = dateFormat.parse(dateString);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        MemberUpdateDTO asd = new MemberUpdateDTO();
        Member byMemberEmail = memberRepository.findByMemberEmail("qwe@qwe.com");
        asd.setMemberName("modName");
        asd.setMemberPhone("123123123");
        asd.setMemberGender("female");
        asd.setMemberBirthday(date);
        memberService.modifyMember(byMemberEmail, asd);

    }

    @Test
    public void 비번변경() throws Exception {
        MemberChangePwDTO asd = new MemberChangePwDTO();
        asd.setNewPw("qweqwe");
        asd.setOldPw("0000");
        asd.setNewPwChk("qweqwe");
        Member byMemberEmail = memberRepository.findByMemberEmail("asd@asd.com");

        memberService.modifyPassword(byMemberEmail, asd);
    }

    @Test
    public void 회원목록() {
        List<Member> allMembers = memberService.getAllMembers();
        log.info("allMembers : {} ",allMembers);
    }
    @Test
    public void 이메일찾기() {
        String memberName = "John";
        String dateString = "2012010";
        List<String> members = memberService.getMemberEmail(memberName, dateString);
        log.info("members : {}", members);

    }
    @Test
    public void 비번찾기() {

    }

}
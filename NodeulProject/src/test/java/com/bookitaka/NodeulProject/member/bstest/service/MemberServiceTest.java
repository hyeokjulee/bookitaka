package com.bookitaka.NodeulProject.member.bstest.service;

import com.bookitaka.NodeulProject.member.bstest.model.Member;
import com.bookitaka.NodeulProject.member.bstest.repository.MemberRepository;
import com.bookitaka.NodeulProject.member.bstest.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;


    @Test
    @Rollback(false)
    public void 회원가입() throws Exception{
        //given
        Member member = new Member();
        member.setMemberEmail("asd@asd.com");
        member.setMemberPassword("0000");
        member.setMemberName("asd");
        member.setMemberPhone("0101");
        member.setMemberGender("male");
        member.setMemberBirthday(new Date());
        member.setMemberRole("asd");

        //when
        Integer savedId =  memberService.join(member);

        //then
        assertEquals(member, memberRepository.findOne(savedId));
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
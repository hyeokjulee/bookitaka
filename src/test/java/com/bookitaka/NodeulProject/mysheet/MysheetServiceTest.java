package com.bookitaka.NodeulProject.mysheet;

import com.bookitaka.NodeulProject.member.model.Member;
import com.bookitaka.NodeulProject.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class MysheetServiceTest {

    @Autowired
    MysheetService mysheetService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void canIDownloadSheetTest() {

        Member member = memberRepository.findByMemberEmail("bbbb@naver.com");
        Mysheet mysheet1 = mysheetService.canIDownloadSheet("bbda5275-6545-46c0-b16d-151801d029fd", member);
        Mysheet mysheet2 = mysheetService.canIDownloadSheet("fdfd", member);

        log.info("you can download = {}", mysheet1);
        log.info("you cannot download = {}", mysheet2);

    }

}
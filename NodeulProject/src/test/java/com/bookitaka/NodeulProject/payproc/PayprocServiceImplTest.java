package com.bookitaka.NodeulProject.payproc;

import com.bookitaka.NodeulProject.member.model.Member;
import com.bookitaka.NodeulProject.member.repository.MemberRepository;
import com.bookitaka.NodeulProject.member.service.MemberService;
import com.bookitaka.NodeulProject.sheet.Sheet;
import com.bookitaka.NodeulProject.sheet.SheetService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@Slf4j
class PayprocServiceImplTest {


    @Autowired
    PayprocService payprocService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    SheetService sheetService;

    @Test
    public void makePayTest() {
        Member member = memberRepository.findByMemberEmail("bbbb@naver.com");
        log.info("test member = {}", member.getMemberEmail());
        PayMakeDto payMakeDto = new PayMakeDto();
        payMakeDto.setPaymentUuid(String.valueOf(UUID.randomUUID()));
        payMakeDto.setPaymentInfo("테스트 정보");
        payMakeDto.setPaymentPrice(2000L);

        List<Long> sheetNoList = new ArrayList<>();
        sheetNoList.add(197L);
        sheetNoList.add(198L);

        payMakeDto.setSheetNoList(sheetNoList);

        payMakeDto.setSheetMeans("카드");

        payprocService.makePay(payMakeDto, member);
    }

}
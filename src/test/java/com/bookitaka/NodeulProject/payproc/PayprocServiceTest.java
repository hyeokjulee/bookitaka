package com.bookitaka.NodeulProject.payproc;

import com.bookitaka.NodeulProject.member.model.Member;
import com.bookitaka.NodeulProject.member.repository.MemberRepository;
import com.bookitaka.NodeulProject.mysheet.MysheetRepository;
import com.bookitaka.NodeulProject.payment.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
class PayprocServiceTest {

    @Autowired
    PayprocService payprocService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MysheetRepository mysheetRepository;

    @Autowired
    PaymentRepository paymentRepository;

    private final String testEmail = "aaa@gmail.com";

    @AfterEach
    void afterTest() {
        mysheetRepository.deleteAll(mysheetRepository.findByMember_MemberEmail(testEmail));
        paymentRepository.deleteAll(paymentRepository.findByMember_MemberEmail(testEmail));
    }

    @Ignore
//    @Test
    public void makePayTest() {
        Member member = memberRepository.findByMemberEmail(testEmail);
        log.info("test member = {}", member.getMemberEmail());
        PayMakeDto payMakeDto = new PayMakeDto();
        payMakeDto.setPaymentUuid(String.valueOf(UUID.randomUUID()));
        payMakeDto.setPaymentPrice(2000L);
        payMakeDto.setPaymentInfo("테스트 정보");
        payMakeDto.setMemberEmail(testEmail);
        payMakeDto.setUsedCouponCnt(0);

        List<Long> sheetNoList = new ArrayList<>();
        sheetNoList.add(1001L);
        sheetNoList.add(1002L);

        payMakeDto.setSheetNoList(sheetNoList);

        payMakeDto.setMySheetMeans("결제");

        payprocService.makePay(payMakeDto);

        long mysheetCount = mysheetRepository.count();
        long paymentCount = paymentRepository.count();
        assertThat(mysheetCount).isEqualTo(2);
        assertThat(paymentCount).isEqualTo(1);
    }

//    @Test
//    public void cancelPayTest() {
//
//    }
}
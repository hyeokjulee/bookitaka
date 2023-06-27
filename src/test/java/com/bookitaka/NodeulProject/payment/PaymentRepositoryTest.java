package com.bookitaka.NodeulProject.payment;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class PaymentRepositoryTest {

    @Autowired
    public PaymentRepository paymentRepository;

    @Test
    public void paymentRepoTest() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("paymentDate").descending());
        Page<Payment> payPage = paymentRepository.findAllByMember_MemberEmailOrderByPaymentDateDesc("bbbb@naver.com", pageable);
        log.info("payContent in repo = {}", payPage.getContent().toArray());
        log.info("payContent in repo = {}", payPage.getTotalElements());
    }

}
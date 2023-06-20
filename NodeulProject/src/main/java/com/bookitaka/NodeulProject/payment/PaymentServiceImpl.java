package com.bookitaka.NodeulProject.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{

    private final PaymentRepository paymentRepository;

    @Override
    public Page<Payment> getMyAllPayment(String memberEmail, PaymentCri paymentCri) {

        Pageable pageable = PageRequest.of(paymentCri.getPageNum()-1, paymentCri.getAmount());
        return paymentRepository.findAllByMember_MemberEmailOrderByPaymentDateDesc(memberEmail, pageable);
    }

    @Override
    public Page<Payment> getAllPaymentForAdmin(PaymentCri paymentCri) {

        Pageable pageable = PageRequest.of(paymentCri.getPageNum(), paymentCri.getAmount());

        return paymentRepository.findAll(pageable);
    }
}

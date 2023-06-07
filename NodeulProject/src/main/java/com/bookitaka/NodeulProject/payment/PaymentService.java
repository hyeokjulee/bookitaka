package com.bookitaka.NodeulProject.payment;

import org.springframework.data.domain.Page;

import java.util.List;

public interface PaymentService {

    public Page<Payment> getMyAllPayment(String memberEmail, PaymentCri paymentCri);

    public Page<Payment> getAllPayment(PaymentCri paymentCri);

}

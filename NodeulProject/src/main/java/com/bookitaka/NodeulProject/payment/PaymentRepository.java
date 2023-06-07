package com.bookitaka.NodeulProject.payment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    public Page<Payment> findAllByMember_MemberEmailOrderByPaymentDateDesc(String memberEmail, Pageable pageable);

}

package com.bookitaka.NodeulProject.coupon;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Integer> {

    List<Coupon> findByMemberEmailOrderByCouponEnddate(String memberEmail);

    int countByMemberEmail(String memberEmail);

    boolean existsByMemberEmailAndCouponEnddateGreaterThan(String memberEmail, LocalDateTime currentDate);
}
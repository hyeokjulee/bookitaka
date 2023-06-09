package com.bookitaka.NodeulProject.coupon;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Integer> {

    List<Coupon> findByMemberEmailOrderByCouponEnddate(String memberEmail);

    int countByMemberEmail(String memberEmail);

    List<Coupon> findAllCoupon(CouponCri cri);
}
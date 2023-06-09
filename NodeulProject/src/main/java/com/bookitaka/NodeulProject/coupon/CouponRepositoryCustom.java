package com.bookitaka.NodeulProject.coupon;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponRepositoryCustom {

    List<Coupon> findAllCoupon(CouponCri cri);
}
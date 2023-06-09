package com.bookitaka.NodeulProject.coupon;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CouponService {
    Coupon addToCoupon(Coupon coupon);
    List<Coupon> getCouponByMemberEmail(String memberEmail);

    int getCountByMemberEmail(String memberEmail);

    public List<Coupon> getAllCoupons(CouponCri cri);
}
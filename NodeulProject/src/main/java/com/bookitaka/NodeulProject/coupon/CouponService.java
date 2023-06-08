package com.bookitaka.NodeulProject.coupon;

import com.bookitaka.NodeulProject.cart.Cart;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CouponService {
    Coupon addToCoupon(Coupon coupon);
    List<Coupon> getCouponByMemberEmail(String memberEmail);
}
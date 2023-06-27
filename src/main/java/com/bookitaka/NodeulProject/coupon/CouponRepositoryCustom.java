package com.bookitaka.NodeulProject.coupon;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponRepositoryCustom {

    List<Coupon> findAllCouponByMemberEmail(CouponCri cri, String email);

    List<Coupon> findAllValidCouponByMemberEmail(String email);

}
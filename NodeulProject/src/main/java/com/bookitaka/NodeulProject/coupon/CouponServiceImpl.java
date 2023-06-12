package com.bookitaka.NodeulProject.coupon;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final CouponRepositoryCustom couponRepositoryCustom;

//    @Override
//    public Coupon addToCoupon(Coupon coupon) {
//        // 쿠폰 생성
//        return couponRepository.save(coupon);
//    }

    @Override
    public List<Coupon> getCouponByMemberEmail(String memberEmail) {
        return couponRepository.findByMemberEmailOrderByCouponEnddate(memberEmail);
    }

    @Override
    public int getCountByMemberEmail(String memberEmail) {
        return couponRepository.countByMemberEmail(memberEmail);
    }

    @Override
    public List<Coupon> getAllCouponsByMemberEmail(CouponCri cri, String memberEmail) {
        return couponRepositoryCustom.findAllCouponByMemberEmail(cri, memberEmail);
    }
}
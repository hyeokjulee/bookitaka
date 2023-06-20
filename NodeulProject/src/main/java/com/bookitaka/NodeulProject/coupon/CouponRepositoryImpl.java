package com.bookitaka.NodeulProject.coupon;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Repository
@Transactional
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepositoryCustom {
    private final JPAQueryFactory qf;

    QCoupon qCoupon = new QCoupon("m");

    @Override
    public List<Coupon> findAllCouponByMemberEmail(CouponCri cri, String email) {
        log.info("cri = {}", cri);

        JPAQuery<Coupon> query = qf.selectFrom(qCoupon);

        query.where(qCoupon.memberEmail.eq(email));

        query.offset((cri.getPageNum() - 1) * cri.getAmount()).limit(cri.getAmount());

        query.orderBy(qCoupon.couponNo.desc());

        return query.fetch();
    }

    @Override
    public List<Coupon> findAllValidCouponByMemberEmail(String email) {

        JPAQuery<Coupon> query = qf.selectFrom(qCoupon);

        query.where(qCoupon.memberEmail.eq(email))
                .where(qCoupon.couponEnddate.after(LocalDateTime.now()))
                .orderBy(qCoupon.couponEnddate.asc());

        return query.fetch();
    }

}
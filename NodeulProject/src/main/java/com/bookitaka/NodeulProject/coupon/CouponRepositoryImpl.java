package com.bookitaka.NodeulProject.coupon;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Repository
@Transactional
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepositoryCustom {
    private final JPAQueryFactory qf;

    QCoupon qCoupon = new QCoupon("m");

    @Override
    public List<Coupon> findAllCoupon(CouponCri cri) {
        log.info("cri = {}", cri);

        JPAQuery<Coupon> query = qf.selectFrom(qCoupon);

        query.offset((cri.getPageNum() - 1) * cri.getAmount()).limit(cri.getAmount());

        return query.fetch();
    }
}
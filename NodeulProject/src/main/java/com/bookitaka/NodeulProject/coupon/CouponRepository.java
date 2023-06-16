package com.bookitaka.NodeulProject.coupon;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Integer> {

    List<Coupon> findByMemberEmailOrderByCouponEnddate(String memberEmail);

    int countByMemberEmail(String memberEmail);

    boolean existsByMemberEmailAndCouponEnddateGreaterThan(String memberEmail, LocalDateTime currentDate);

    @Modifying
    @Query("UPDATE Coupon c SET c.couponLeft = c.couponLeft - :amount WHERE c.couponNo = :couponNo")
    int updateUsedCoupon(@Param("couponNo") Long couponNo, @Param("amount") int amount);


}
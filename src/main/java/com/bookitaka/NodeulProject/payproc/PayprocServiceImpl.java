package com.bookitaka.NodeulProject.payproc;

import com.bookitaka.NodeulProject.coupon.Coupon;
import com.bookitaka.NodeulProject.coupon.CouponRepository;
import com.bookitaka.NodeulProject.coupon.CouponRepositoryCustom;
import com.bookitaka.NodeulProject.member.model.Member;
import com.bookitaka.NodeulProject.member.repository.MemberRepository;
import com.bookitaka.NodeulProject.payment.Payment;
import com.bookitaka.NodeulProject.payment.PaymentRepository;
import com.bookitaka.NodeulProject.sheet.SheetRepository;
import com.bookitaka.NodeulProject.mysheet.Mysheet;
import com.bookitaka.NodeulProject.mysheet.MysheetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PayprocServiceImpl implements PayprocService {

    private final PaymentRepository paymentRepository;
    private final SheetRepository sheetRepository;

    private final CouponRepository couponRepository;
    private final CouponRepositoryCustom couponRepositoryCustom;
    private final MysheetRepository mysheetRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public boolean makePay(PayMakeDto payMakeDto) {

        //payment 등록.
        Payment payment = new Payment();
        payment.setPaymentUuid(payMakeDto.getPaymentUuid()); //미리 받은 번호.
        payment.setPaymentPrice(payMakeDto.getPaymentPrice());
        payment.setPaymentInfo(payMakeDto.getPaymentInfo());
        Member member = memberRepository.findByMemberEmail(payMakeDto.getMemberEmail());

        payment.setMember(member);

        Payment save = paymentRepository.save(payment);
        log.info("makePay pay = {}", save);

        //쿠폰 사용하기
        List<Coupon> coupons = couponRepositoryCustom.findAllValidCouponByMemberEmail(payMakeDto.getMemberEmail());
        log.info("makePay coupons = {}", coupons);
        int usedCouponCnt = payMakeDto.getUsedCouponCnt();
        for (Coupon coupon : coupons) { //남은거 전체 for문
            int leftCoupon = coupon.getCouponLeft();
            if (leftCoupon >= usedCouponCnt) {
                //현재 쿠폰 남은양이 더 많다면
                coupon.setCouponLeft(leftCoupon - usedCouponCnt);
                //그냥 빼기
                couponRepository.save(coupon);
                break;
            } else { //현재 쿠폰 남은양이 더 적다면
                coupon.setCouponLeft(0); //현재꺼는 0으로
                usedCouponCnt -= leftCoupon; //남은 숫자
                couponRepository.save(coupon);
            }
        }
        
        // 현재 시간
        Date nowDate = new Date();

        // 일주일 뒤의 시간
        long oneWeekInMillis = 7 * 24 * 60 * 60 * 1000; // 1주일을 밀리초로 변환
        long oneWeekLaterInMillis = System.currentTimeMillis() + oneWeekInMillis;
        Date afterWeekDate = new Date(oneWeekLaterInMillis);

        List<Long> sheetNoList = payMakeDto.getSheetNoList();
        for (Long sheetNo:sheetNoList) {
            //Mysheet에 리스트 전체를 등록 + 구매 수 업데이트
            Mysheet mysheet = new Mysheet();

            mysheet.setMember(member);
            mysheet.setMysheetMeans(payMakeDto.getMySheetMeans());

            mysheet.setMysheetStartdate(nowDate);
            mysheet.setMysheetEnddate(afterWeekDate);

            mysheet.setSheet(sheetRepository.findSheetByNo(Math.toIntExact(sheetNo)).orElse(null));

            log.info("Payproc member = {}", member);
            //구매수 업데이트
            sheetRepository.plusOneSheetBuyCnt(sheetNo.intValue());

            mysheetRepository.save(mysheet);
        }

        return true;
    }

    @Override
    @Transactional
    public boolean makeCouponPay(PayMakeDto payMakeDto) {
        //payment 등록.
        Payment payment = new Payment();
        payment.setPaymentUuid(payMakeDto.getPaymentUuid()); //미리 받은 번호.
        payment.setPaymentPrice(payMakeDto.getPaymentPrice());
        payment.setPaymentInfo(payMakeDto.getPaymentInfo());
        Member member = memberRepository.findByMemberEmail(payMakeDto.getMemberEmail());
        payment.setMember(member);

        Payment save = paymentRepository.save(payment);
        log.info("makePay pay = {}", save);

        //coupon 생성.
        Coupon coupon = new Coupon();
        coupon.setMemberEmail(member.getMemberEmail());

        // 현재 날짜와 시간을 가져옵니다.
        LocalDateTime now = LocalDateTime.now();

        // 현재로부터 1년을 더합니다.
        LocalDateTime oneYearLater = now.plusYears(1);

        // 시간을 11시 59분으로 설정합니다.
        LocalDateTime result = oneYearLater.withHour(11).withMinute(59);

        coupon.setCouponEnddate(result);

        log.info("Payproc member = {}", member);

        couponRepository.save(coupon);

        return true;
    }
}
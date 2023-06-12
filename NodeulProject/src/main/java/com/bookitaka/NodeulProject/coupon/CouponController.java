package com.bookitaka.NodeulProject.coupon;

import com.bookitaka.NodeulProject.cart.Cart;
import com.bookitaka.NodeulProject.cart.CartService;
import com.bookitaka.NodeulProject.member.model.Member;
import com.bookitaka.NodeulProject.member.service.MemberService;
import com.bookitaka.NodeulProject.sheet.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MEMBER')")
@Controller
@RequestMapping("/coupon")
@RequiredArgsConstructor
public class CouponController {
    private final MemberService memberService;
    private final CouponService couponService;
    private final HttpServletRequest request;

    @GetMapping("/buy") // 쿠폰구매 페이지
    public String buyCoupon() {
        return "coupon/couponPaying"; // 뷰 이름을 반환
    }

    @GetMapping("/myCoupon") // 내쿠폰 페이지
    public String myCoupon() {
        return "coupon/myCoupon"; // 뷰 이름을 반환
    }

    @GetMapping("/couponPayComplete") // 쿠폰 결제 완료 페이지
    public String couponPayComplete() {
        return "coupon/couponPayComplete"; // 뷰 이름을 반환
    }

    @GetMapping("/list") // 쿠폰리스트
    public String couponList(@RequestParam(name = "pageNum", defaultValue = "1") int page,
                             @RequestParam(name = "amount", defaultValue = "5") int amount,
                             Model model) {

        CouponCri cri = new CouponCri(page, amount);
        String email = request.getRemoteUser();
        int totalNum = couponService.getCountByMemberEmail(email);

        model.addAttribute("couponList", couponService.getAllCouponsByMemberEmail(cri, email));
        model.addAttribute("pageInfo", new CouponPageInfo(cri, totalNum));
        model.addAttribute("cri", cri);

        return "coupon/couponList"; // 뷰 이름을 반환
    }

//    @PostMapping("/couponCreate") // 쿠폰 생성
//    @ResponseBody
//    public ResponseEntity<Map<String, Object>> couponCreate() {
//        Map<String, Object> response = new HashMap<>();
//        String email = request.getRemoteUser();
//
//        Coupon coupon = new Coupon();
//        coupon.setMemberEmail(email);
//        couponService.addToCoupon(coupon);
//        response.put("success", true);
//
//        return ResponseEntity.ok(response);
//    }

    @GetMapping("/getCount") // 남은 쿠폰 개수 반환
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getCount() {
        Map<String, Object> response = new HashMap<>();
        String email = request.getRemoteUser();
        List<Coupon> coupons = couponService.getCouponByMemberEmail(email);
        Coupon lastCoupon = coupons.get(coupons.size() - 1);
        int couponcount = lastCoupon.getCouponLeft();

        response.put("success", true);
        response.put("couponcount", couponcount);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/myInfo") // 내 정보
    @ResponseBody
    public ResponseEntity<Map<String, Object>> myInfo() {
        Map<String, Object> response = new HashMap<>();

        // 내 정보
        String email = request.getRemoteUser();
        Member member = memberService.search(email);
        response.put("success", true);
        response.put("member", member);

        return ResponseEntity.ok(response);
    }
}
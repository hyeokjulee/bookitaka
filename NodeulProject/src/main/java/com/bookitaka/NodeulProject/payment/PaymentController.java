package com.bookitaka.NodeulProject.payment;

import com.bookitaka.NodeulProject.member.security.Token;
import com.bookitaka.NodeulProject.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final MemberService memberService;

    @GetMapping("/myPay")
    public String showMyPayment(HttpServletRequest request,
                                @RequestParam(name = "pageNum", defaultValue = "1") int page,
                                @RequestParam(name = "amount", defaultValue = "10") int amount,
                                Model model) {

        PaymentCri paymentCri = new PaymentCri(page, amount);
        String myEmail = memberService.whoami(request.getCookies(), Token.ACCESS_TOKEN).getMemberEmail();
        log.info("myEmail = {}", myEmail);
        Page<Payment> myAllPaymentPage = paymentService.getMyAllPayment(myEmail, paymentCri);

        List<Payment> paymentList = myAllPaymentPage.getContent();
        log.info("controller paymentList = {}", paymentList);
        model.addAttribute("myPayList", paymentList);
        model.addAttribute("pageInfo", new PaymentPageInfo(paymentCri, myAllPaymentPage.getTotalPages()));

        return "myPage/myPay";
    }

}

package com.bookitaka.NodeulProject.faq;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/faq")
public class FaqController {

//    private static final FaqService service;

    @GetMapping("/qna")
    public String qna() {
        return "/qna";
    }

    @GetMapping
    public String list() {
        return "/list";
    }

    @GetMapping("/register")
    public String register() {
        return "/registerForm";
    }

    @GetMapping("/{faqNo}")
    public String modify(Long faqNo) {
        return "get faqNo=" + faqNo;
    }


}

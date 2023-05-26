package com.bookitaka.NodeulProject.faq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
@RequestMapping(value = "/faq")
@RequiredArgsConstructor
public class FaqController {

    private final FaqService service;

    // 1대1 문의 정적 페이지
    @GetMapping("/qna")
    public String qna() {
        return "/faq/qna";
    }

    // FAQ 카테고리 별 목록
    @GetMapping("/{faqCategory}")
    public String list(@PathVariable String faqCategory, Model model) {
        model.addAttribute("faqAllCategory", service.getAllFaqCategory());
        model.addAttribute("faqCategory", faqCategory);
//        model.addAttribute("faqList", service.getAllFaq());
        model.addAttribute("faqListByCategory", service.getAllFaqByFaqCategory(faqCategory));
        return "/faq/faqList";
    }

    // FAQ most 목록 - 기본 페이지
    @GetMapping
    public String listBest(Model model) {
        model.addAttribute("faqAllCategory", service.getAllFaqCategory());
        model.addAttribute("faqListByBest", service.getAllFaqByFaqBest());
        return "/faq/faqList";
    }

    // FAQ 등록 폼
    @GetMapping("/add")
    public String addForm(Model model, FaqCategory faqCategory) {
        model.addAttribute("faqAllCategory", service.getAllFaqCategory());
        return "/faq/faqAddForm";
    }

    // FAQ 등록 처리
    @PostMapping("/add")
    public String addProc(Model model, @ModelAttribute Faq faq, HttpServletResponse response) {
        log.info("Controller addProc : faq = " + faq);
        service.registerFaq(faq);
        model.addAttribute("statusCode", response.getStatus());
        return "redirect:/faq";
    }

    // FAQ 삭제 처리
    @PostMapping("/remove/{faqNo}")
    public String remove(Model model, @PathVariable Long faqNo, HttpServletResponse response) {
        Faq removefaq = service.getOneFaq(faqNo).get();
        service.removeFaq(removefaq);
        model.addAttribute("statusCode", response.getStatus());
        return "redirect:/faq";
    }






}

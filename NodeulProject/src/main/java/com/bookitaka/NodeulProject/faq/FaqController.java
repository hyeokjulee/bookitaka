package com.bookitaka.NodeulProject.faq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Slf4j
@Controller
@RequestMapping(value = "/faq")
@RequiredArgsConstructor
public class FaqController {

    private final FaqService service;
    private final ModelMapper modelMapper;

    // 1대1 문의 정적 페이지
    @GetMapping("/qna")
    public String qna() {
        return "/faq/qna";
    }

    int size = 5;

    // FAQ 카테고리 별 목록
    @GetMapping("/{faqCategory}")
    public String list(@PathVariable String faqCategory, Model model, @RequestParam(name = "page", defaultValue = "0") int page) {
        model.addAttribute("faqCategory", faqCategory);
        model.addAttribute("faqAllCategory", service.getAllFaqCategory());

        Pageable pageable = PageRequest.of(page, size);
        if (faqCategory.equals("best")) {
            model.addAttribute("faqAll", service.getAllFaqByFaqBest(pageable));
        } else {
            model.addAttribute("faqAll", service.getAllFaqByFaqCategory(faqCategory, pageable));
        }
//        model.addAttribute("faqList", service.getAllFaq());
        return "/faq/faqList";
    }

    // FAQ 등록 폼
    @GetMapping("/add")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addForm(Model model) {
        model.addAttribute("faqAllCategory", service.getAllFaqCategory());
        return "/faq/faqAddForm";
    }

    // FAQ 등록 처리
    @PostMapping("/add")
    public String addProc(Model model, @ModelAttribute FaqRegisterDto faqRegisterDto, HttpServletResponse response) throws UnsupportedEncodingException {
        log.info("Controller addProc : faqRegisterDto = " + faqRegisterDto);

        // faqDto -> faq 변환
        Faq faq = modelMapper.map(faqRegisterDto, Faq.class);
        service.registerFaq(faq);

        model.addAttribute("statusCode", response.getStatus());
        return "redirect:/faq/" + URLEncoder.encode(faqRegisterDto.getFaqCategory(), "UTF-8");
    }

    // FAQ 삭제 처리
    @PostMapping("/remove/{faqNo}")
    public String remove(Model model, @PathVariable Long faqNo, HttpServletResponse response) throws UnsupportedEncodingException {
        Faq removefaq = service.getOneFaq(faqNo).get();
        service.removeFaq(removefaq);
        model.addAttribute("statusCode", response.getStatus());
        return "redirect:/faq/" + URLEncoder.encode(removefaq.getFaqCategory(), "UTF-8");
    }

    // FAQ 수정 폼
    @GetMapping("/edit/{faqNo}")
    public String editForm(Model model, @PathVariable Long faqNo) {
        model.addAttribute("faqAllCategory", service.getAllFaqCategory());
        Faq editFaq = service.getOneFaq(faqNo).get();
        model.addAttribute("editFaq", editFaq);
        return "/faq/faqEditForm";
    }

    // FAQ 수정 처리
    @PostMapping("/edit")
    public String editProc(Model model, @ModelAttribute FaqModifyDto faqModifyDto, HttpServletResponse response) throws UnsupportedEncodingException {
        log.info("Controller editProc : faqModifyDto = " + faqModifyDto);

        // faqDto -> faq 변환
        Faq faq = modelMapper.map(faqModifyDto, Faq.class);
        service.modifyFaq(faq);

        model.addAttribute("statusCode", response.getStatus());
        return "redirect:/faq/" + URLEncoder.encode(faq.getFaqCategory(), "UTF-8");
    }


}

package com.bookitaka.NodeulProject.faq;

import com.bookitaka.NodeulProject.member.controller.MemberAPIController;
import com.bookitaka.NodeulProject.member.model.Member;
import com.bookitaka.NodeulProject.member.model.MemberRoles;
import com.bookitaka.NodeulProject.member.security.Token;
import com.bookitaka.NodeulProject.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Slf4j
@Controller
@RequestMapping(value = "/faq")
@RequiredArgsConstructor
public class FaqController {

    private final FaqService service;
    private final ModelMapper modelMapper;
    private final MemberService memberService;

    // 1대1 문의 정적 페이지
    @GetMapping("/qna")
    public String qna() {
        return "/faq/qna";
    }

    int size = 5;

    // FAQ 카테고리 별 목록
    @GetMapping("/{faqCategory}")
    public String list(@PathVariable String faqCategory,
                       @RequestParam(name = "page", defaultValue = "0") int page,
                       @RequestParam(name = "keyword", defaultValue = "") String keyword,
                       HttpServletRequest request, Model model) {
        model.addAttribute("faqCategory", faqCategory);
        model.addAttribute("faqAllCategory", service.getAllFaqCategory());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        Pageable pageable = PageRequest.of(page, size);

        log.info("================================================ keyword : {}", keyword);
        int currentGroup = page / size;
        int startPage = currentGroup * size;
        int endPage;
        int totalPages;

        model.addAttribute("startPage", startPage);
        model.addAttribute("keyword", keyword);
        if(keyword.isBlank()) {
            totalPages = service.getAllFaqByFaqCategory(faqCategory, pageable).getTotalPages();
            endPage = Math.min(startPage + size, totalPages) - 1;
            model.addAttribute("faqAll", service.getAllFaqByFaqCategory(faqCategory, pageable));
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("endPage", endPage);
        } else {
            totalPages = service.getAllFaqContaningKeyword(keyword, pageable).getTotalPages();
            endPage = Math.min(startPage + size, totalPages) - 1;
            model.addAttribute("faqAll", service.getAllFaqContaningKeyword(keyword, pageable));
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("endPage", endPage);
        }

        // 이전 그룹의 첫 번째 페이지로 이동
        int previousGroupStartPage = (currentGroup == 0) ? 0 : (currentGroup - 1) * size;
        model.addAttribute("previousGroupStartPage", previousGroupStartPage);

        // 다음 그룹의 첫 번째 페이지로 이동
        int nextGroupStartPage = (currentGroup + 1) * size;

        if (totalPages % 5 == 0 && startPage == totalPages - 5) {
            nextGroupStartPage = endPage;
        } else if (nextGroupStartPage > endPage + 1) {
            nextGroupStartPage = endPage;
        }
        model.addAttribute("nextGroupStartPage", nextGroupStartPage);

        return "/faq/faqList";
    }

    // FAQ 등록 폼
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/add")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addForm(Model model) {
        model.addAttribute("faqRegisterDto", new FaqRegisterDto());
        model.addAttribute("faqAllCategory", service.getAllFaqCategory());
        return "/faq/faqAddForm";
    }

    // FAQ 등록 처리
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<?> addProc(Model model,
                                     @Validated @ModelAttribute FaqRegisterDto faqRegisterDto,
                                     BindingResult bindingResult) {
        log.info("Controller addProc : faqRegisterDto = " + faqRegisterDto);
        // faqDto -> faq 변환
        Faq faq = modelMapper.map(faqRegisterDto, Faq.class);
        boolean result = service.registerFaq(faq);
        model.addAttribute("faqAllCategory", service.getAllFaqCategory());

        // validation 오류
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        // 등록 실패 오류
        if (!result) {
            return ResponseEntity.unprocessableEntity().build();
        }
        return ResponseEntity.ok().build();
    }

    // FAQ 삭제 처리
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/remove/{faqNo}")
    public String remove(Model model, @PathVariable Long faqNo, HttpServletResponse response) throws UnsupportedEncodingException {
        Faq removefaq = service.getOneFaq(faqNo).get();
        service.removeFaq(removefaq);
        model.addAttribute("statusCode", response.getStatus());
        return "redirect:/faq/" + URLEncoder.encode(removefaq.getFaqCategory(), "UTF-8");
    }

    // FAQ 수정 폼
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/edit/{faqNo}")
    public String editForm(Model model, @PathVariable Long faqNo) {
        model.addAttribute("faqAllCategory", service.getAllFaqCategory());
        Faq editFaq = service.getOneFaq(faqNo).get();
        model.addAttribute("editFaq", editFaq);
        return "/faq/faqEditForm";
    }

    // FAQ 수정 처리
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/edit")
    public ResponseEntity<?> editProc(@Validated @ModelAttribute FaqModifyDto faqModifyDto, BindingResult bindingResult, Model model) {
        log.info("Controller editProc : faqModifyDto = " + faqModifyDto);

        // faqDto -> faq 변환
        Faq faq = modelMapper.map(faqModifyDto, Faq.class);
        service.modifyFaq(faq);
        model.addAttribute("faqAllCategory", service.getAllFaqCategory());

        // validation 오류
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        return ResponseEntity.ok().build();
    }


}

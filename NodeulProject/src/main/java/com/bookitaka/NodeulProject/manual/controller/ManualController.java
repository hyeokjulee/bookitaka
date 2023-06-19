package com.bookitaka.NodeulProject.manual.controller;

import com.bookitaka.NodeulProject.manual.dto.ManualDto;
import com.bookitaka.NodeulProject.manual.service.ManualService;
import com.bookitaka.NodeulProject.notice.dto.NoticeDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/manual")
public class ManualController {

    private static final int PAGE_SIZE = 10;
    private ManualService manualService;

    @GetMapping("/list")
    public String list(Model model,
                       @RequestParam(name = "page", defaultValue = "0") int page,
                       @RequestParam(name = "keyword", defaultValue = "") String keyword) {
        Sort sort = Sort.by("manualNo").descending();
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, sort);
        Page<ManualDto> manualList = manualService.getManualList(pageable, keyword);

        model.addAttribute("keyword", keyword);
        model.addAttribute("manualList", manualList);

        model.addAttribute("totalPages", manualList.getTotalPages());
        model.addAttribute("currentPage", page);
        int size = 10;
        int currentGroup = page / size;
        int startPage = currentGroup * size;
        int totalPages = manualList.getTotalPages();
        int endPage = Math.min(startPage + size, manualList.getTotalPages()) - 1;

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        // 이전 그룹의 첫 번째 페이지로 이동
        int previousGroupStartPage = (currentGroup == 0) ? 0 : (currentGroup - 1) * size;
        model.addAttribute("previousGroupStartPage", previousGroupStartPage);

        // 다음 그룹의 첫 번째 페이지로 이동
        int nextGroupStartPage = (currentGroup + 1) * size;

        if (totalPages % size == 0 && startPage == totalPages - size) {
            nextGroupStartPage = endPage;
        } else if (nextGroupStartPage > endPage + 1) {
            nextGroupStartPage = endPage;
        }
        model.addAttribute("nextGroupStartPage", nextGroupStartPage);
        return "manual/list";
    }

    @GetMapping("/post")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public String write(Model model) {
        model.addAttribute("manualDto", new ManualDto());
        return "manual/write.html";
    }

    @PostMapping("/post")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public String write(@Validated @ModelAttribute ManualDto manualDto, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {

            return "manual/write.html"; // 유효성 검사에 실패한 경우에 대한 에러 페이지를 반환하거나 다른 처리를 수행할 수 있습니다.
        }

        manualService.registerManual(manualDto);
        return "redirect:/manual/list";
    }

    @GetMapping("/post/{manualNo}")
    public String detail(@PathVariable("manualNo") Integer manualNo, Model model) {
        ManualDto manualDto = manualService.getManual(manualNo);
        model.addAttribute("manualDto", manualDto);

        return "manual/detail.html";
    }

    @GetMapping("/post/edit/{manualNo}")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public String edit(@PathVariable("manualNo") Integer manualNo, Model model) {
        ManualDto manualDto = manualService.getManual(manualNo);
        model.addAttribute("manualDto", manualDto);

        return "manual/update.html";
    }

    @PostMapping("/post/edit/{manualNo}")
//@PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateManual(@Validated @ModelAttribute("manualDto") ManualDto manualDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "manual/update.html";
        }
        manualService.registerManual(manualDto);
        return "redirect:/manual/list";
    }

    @DeleteMapping("/post/{manualNo}")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delete(@PathVariable("manualNo") Integer manualNo) {
        manualService.deleteManual(manualNo);

        return "redirect:/manual/list";
    }
}

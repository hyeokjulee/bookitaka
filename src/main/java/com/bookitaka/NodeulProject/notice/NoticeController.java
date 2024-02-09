package com.bookitaka.NodeulProject.notice;

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

@Controller
@AllArgsConstructor
@RequestMapping("/notice")
public class NoticeController {

    private static final int PAGE_SIZE = 10;
    private NoticeService noticeService;

    @GetMapping("/list")
    public String list(Model model,
                       @RequestParam(name="page", defaultValue = "0") int page,
                       @RequestParam(name = "keyword", defaultValue = "") String keyword) {
        Sort sort = Sort.by("noticeRegdate").descending();
        Pageable pageable = PageRequest.of(page, PAGE_SIZE,sort);
        Page<NoticeDto> noticeList = noticeService.getNoticeList(pageable, keyword);

        model.addAttribute("noticeList", noticeList);
        model.addAttribute("keyword", keyword);

        model.addAttribute("totalPages", noticeList.getTotalPages());
        model.addAttribute("currentPage", page);
        int size = 10;
        int currentGroup = page / size;
        int startPage = currentGroup * size;
        int totalPages = noticeList.getTotalPages();
        int endPage = Math.min(startPage + size, noticeList.getTotalPages()) - 1;

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
        return "notice/list.html";
    }


    @GetMapping("/post")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String write(Model model){
        model.addAttribute("noticeDto", new NoticeDto()); // noticeDto 객체를 요청 속성에 추가
        return "notice/write.html"; }

    @PostMapping("/post")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String write(@Validated @ModelAttribute NoticeDto noticeDto, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()) {

            return "notice/write.html"; // 유효성 검사에 실패한 경우에 대한 에러 페이지를 반환하거나 다른 처리를 수행할 수 있습니다.
        }
        noticeService.registerNotice(noticeDto);
        return "redirect:/notice/list";
    }

    @GetMapping("/post/{noticeNo}")
    public String detail(@PathVariable("noticeNo") Integer noticeNo, Model model) {
        NoticeDto noticeDto = noticeService.getNotice(noticeNo);
        noticeService.updateHit(noticeNo);
        model.addAttribute("noticeDto", noticeDto);
        return "notice/detail.html";
    }

    @GetMapping("/post/edit/{noticeNo}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String edit(@PathVariable("noticeNo") Integer noticeNo, Model model) {
        NoticeDto noticeDto = noticeService.getNotice(noticeNo);
        model.addAttribute("noticeDto", noticeDto);
        return "notice/update.html";
    }

    @PostMapping("/post/edit/{noticeNo}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateNotice(@PathVariable("noticeNo") Integer noticeNo, @Validated @ModelAttribute("noticeDto") NoticeDto noticeDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "notice/update.html";
        }
        noticeService.updateNotice(noticeNo, noticeDto.toEntity());
        return "redirect:/notice/list/";
    }

    @DeleteMapping("/post/{noticeNo}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delete(@PathVariable("noticeNo") Integer noticeNo) {
        noticeService.removeNotice(noticeNo);

        return "redirect:/notice/list";
    }
}

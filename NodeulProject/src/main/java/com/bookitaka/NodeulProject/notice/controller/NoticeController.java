package com.bookitaka.NodeulProject.notice.controller;

import com.bookitaka.NodeulProject.notice.domain.entity.Notice;
import com.bookitaka.NodeulProject.notice.dto.NoticeDto;
import com.bookitaka.NodeulProject.notice.service.NoticeService;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/notice")
public class NoticeController {
    private NoticeService noticeService;

    @GetMapping("/list")
    public String list(Model model) {
        List<NoticeDto> noticeList = noticeService.getNoticeList();
        model.addAttribute("noticeList", noticeList);
        return "notice/list.html";
    }

    @GetMapping("/post")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String write(){ return "notice/write.html"; }

    @PostMapping("/post")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String write(@Validated NoticeDto noticeDto, BindingResult bindingResult){
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

    @PutMapping("/post/edit/{noticeNo}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateNotice(@PathVariable("noticeNo") Integer noticeNo, Notice notice ) {
        noticeService.updateNotice(noticeNo, notice);
        return "redirect:/notice/list/";
    }

    @DeleteMapping("/post/{noticeNo}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delete(@PathVariable("noticeNo") Integer noticeNo) {
        noticeService.removeNotice(noticeNo);

        return "redirect:/notice/list";
    }

    @GetMapping("/search")
    public String search(@RequestParam(value="keyword")String keyword, Model model){
        List<NoticeDto> noticeDtoList = noticeService.searchNotice(keyword);

        model.addAttribute("noticeList",noticeDtoList);

        return "notice/list.html";
    }

}

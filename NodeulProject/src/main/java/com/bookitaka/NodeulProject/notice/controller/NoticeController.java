package com.bookitaka.NodeulProject.notice.controller;

import com.bookitaka.NodeulProject.notice.dto.NoticeDto;
import com.bookitaka.NodeulProject.notice.service.NoticeService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
public class NoticeController {

    private NoticeService noticeService;

    @GetMapping("/")
    public String list(Model model){
        List<NoticeDto> noticeList = noticeService.getNoticelist();

        model.addAttribute("noticeList", noticeList);
        return "notice/list.html";
    }

    @GetMapping("/post")
    public String write(){ return "notice/write.html"; }

    @PostMapping("/post")
    public String write(NoticeDto noticeDto){
        noticeService.savePost(noticeDto);
        return "redirect:/";
    }

    @GetMapping("/post/{noticeNo}")
    public String detail(@PathVariable("noticeNo") Integer noticeNo, Model model) {
        NoticeDto noticeDto = noticeService.getPost(noticeNo);
        model.addAttribute("noticeDto", noticeDto);
        return "notice/detail.html";
    }

    @GetMapping("/post/edit/{noticeNo}")
    public String edit(@PathVariable("noticeNo") Integer noticeNo, Model model) {
        NoticeDto noticeDto = noticeService.getPost(noticeNo);

        model.addAttribute("noticeDto", noticeDto);

        return "notice/update.html";
    }

    @PutMapping("/post/edit/{noticeNo}")
    public String update(NoticeDto noticeDto) {
        noticeService.savePost(noticeDto);

        return "redirect:/";
    }

    @DeleteMapping("/post/{noticeNo}")
    public String delete(@PathVariable("noticeNo") Integer noticeNo) {
        noticeService.deletePost(noticeNo);

        return "redirect:/";
    }

    @GetMapping("/notice/search")
    public String search(@RequestParam(value="keyword")String keyword,Model model){
        List<NoticeDto> noticeDtoList = noticeService.searchPost(keyword);

        model.addAttribute("noticeList",noticeDtoList);

        return "notice/list.html";
    }
}

package com.bookitaka.NodeulProject.notice.controller;

import com.bookitaka.NodeulProject.notice.dto.NoticeDto;
import com.bookitaka.NodeulProject.notice.service.NoticeService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class NoticeController {

    private NoticeService noticeService;

    @GetMapping("/")
    public String list(){
        return "notice/list.html";
    }

    @GetMapping("/post")
    public String write(){ return "notice/write.html"; }

    @PostMapping("/post")
    public String write(NoticeDto noticeDto){
        noticeService.savePost(noticeDto);
        return "redirect:/";
    }
}

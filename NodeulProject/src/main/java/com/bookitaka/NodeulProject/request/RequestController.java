package com.bookitaka.NodeulProject.request;

import com.bookitaka.NodeulProject.member.model.Member;
import com.bookitaka.NodeulProject.member.security.Token;
import com.bookitaka.NodeulProject.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/sheet/request")
@RequiredArgsConstructor
@Slf4j
public class RequestController {

    private final RequestService requestService;
    private final MemberService memberService;


    @GetMapping("/booksearch")
    @ResponseBody
    public Map<String, Object> bookSearch(@RequestParam("keyword") String keyword,
                                          @RequestParam("authorSearch") String authorSearch,
                                          @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                                          Model model ) {
//        log.info("keyword = {}", keyword);
//        log.info("authorSearch = {}", authorSearch);
        log.info("pageNum = {}", pageNum);

        String currentPageNum = "";
        String total = "";

        return requestService.searchBook(keyword, authorSearch, pageNum);
    }


    @GetMapping
    public String requestForm(Model model,
                              HttpServletRequest request) {
        Member currentMember = memberService.whoami(request.getCookies(), Token.ACCESS_TOKEN);
        model.addAttribute("currentMember", currentMember);
        return "request/requestForm";
    }
    @PostMapping
    public String requestProc() {

        return "request/requestForm";
    }

    @GetMapping("/myrequest")
    public String listMyRequest() {

        return "request/myRequestList";
    }

    @GetMapping("/list")
    public String listRequestForAdmin() {

        return "request/requestList";
    }

}
package com.bookitaka.NodeulProject.request;

import com.bookitaka.NodeulProject.member.model.Member;
import com.bookitaka.NodeulProject.member.security.Token;
import com.bookitaka.NodeulProject.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/sheet/request")
@RequiredArgsConstructor
@Slf4j
public class RequestController {

    private final RequestService requestService;
    private final MemberService memberService;
    private final ModelMapper modelMapper;


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
    public String requestProc(Model model,
                              @ModelAttribute RequestDto requestDto,
                              HttpServletResponse response) {
        log.info("Request Controller - requestProc 호출");
        log.info("requestDto = {}", requestDto);
        Request request = modelMapper.map(requestDto, Request.class);
        log.info("request = {}", request);
        requestService.registerRequest(request);

        model.addAttribute("statusCode", response.getStatus());
        return "redirect:/sheet/request/myrequest";
    }

    @GetMapping("/myrequest")
    public String listMyRequest(Model model,
                                HttpServletRequest request,
                                @RequestParam(name = "page", defaultValue = "0") int page) {
        Member currentMember = memberService.whoami(request.getCookies(), Token.ACCESS_TOKEN);
        model.addAttribute("currentMember", currentMember);

        int size = 3;
        Pageable pageable = PageRequest.of(page, size);

        Page<Request> myRequest = requestService.getMyRequest(currentMember, pageable);
        model.addAttribute("myRequest", myRequest);
        return "request/myRequest";
    }

    @GetMapping("/list")
    public String listRequestForAdmin(@RequestParam(name = "requestIsdone", defaultValue = "0") int requestIsdone,
                                      @RequestParam(name = "page", defaultValue = "0") int page,
                                      Model model) {
        int size = 3;
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "requestRegdate"));
        Page<Request> requestList = requestService.getAllRequestByRequestIsdone(requestIsdone, pageable);
        model.addAttribute("requestList", requestList);
        model.addAttribute("requestIsdone", requestIsdone);
        return "request/requestList";
    }

}
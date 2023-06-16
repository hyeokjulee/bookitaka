package com.bookitaka.NodeulProject.request;

import com.bookitaka.NodeulProject.faq.Faq;
import com.bookitaka.NodeulProject.faq.FaqRegisterDto;
import com.bookitaka.NodeulProject.member.model.Member;
import com.bookitaka.NodeulProject.member.security.Token;
import com.bookitaka.NodeulProject.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        return "request/myRequestList";
    }

    @GetMapping("/myrequest")
    public String listMyRequest() {

        return "request/myRequestList";
    }

    @GetMapping("/list")
    public String listRequestForAdmin(@RequestParam(name = "requestIsdone", defaultValue = "0") int requestIsdone,
                                      @RequestParam(name = "page", defaultValue = "0") int page,
                                      Model model) {
        int size = 3;
        Pageable pageable = PageRequest.of(page, size);
        Page<Request> requestList = requestService.getAllRequestByRequestIsdone(requestIsdone, pageable);
        model.addAttribute("requestList", requestList);
        return "request/requestList";
    }

    @GetMapping("/detail/{requestNo}")
    public String requestDetail(@PathVariable Long requestNo, Model model){
        Request request = requestService.getOneRequest(requestNo).orElse(null);
        log.info("requestDetail request = {}", request);
        model.addAttribute("request", request);
        return "request/requestDetail";
    }

}
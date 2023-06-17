package com.bookitaka.NodeulProject.request;

import com.bookitaka.NodeulProject.member.model.Member;
import com.bookitaka.NodeulProject.member.security.Token;
import com.bookitaka.NodeulProject.member.service.MemberService;
import com.bookitaka.NodeulProject.sheet.SheetRegDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MEMBER')")
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
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MEMBER')")
    public String requestForm(Model model,
                              HttpServletRequest request) {
        model.addAttribute("requestDto", new RequestDto());
        Member currentMember = memberService.whoami(request.getCookies(), Token.ACCESS_TOKEN);
        model.addAttribute("currentMember", currentMember);
        return "request/requestForm";
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MEMBER')")
    public String requestProc(Model model,
                              HttpServletRequest httpServletRequest,
                              @Validated @ModelAttribute RequestDto requestDto,
                              BindingResult bindingResult) {
//        log.info("requestDto = {}", requestDto);
        Request request = modelMapper.map(requestDto, Request.class);
        log.info("request = {}", request);
        requestService.registerRequest(request);

        // validation 오류
        if(bindingResult.hasErrors()){
            log.info("errors={}", bindingResult);
            Member currentMember = memberService.whoami(httpServletRequest.getCookies(), Token.ACCESS_TOKEN);
            model.addAttribute("currentMember", currentMember);
            return "request/requestForm";
        }

        // 등록 실패 오류
//        if (!result) {
//            return ResponseEntity.unprocessableEntity().build();
//        }

        return "redirect:/sheet/request/myrequest";
    }

    @GetMapping("/myrequest")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MEMBER')")
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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

    @GetMapping("/detail/{requestNo}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MEMBER')")
    public String requestDetail(@PathVariable Long requestNo, Model model){
        Request request = requestService.getOneRequest(requestNo).orElse(null);
        log.info("requestDetail request = {}", request);
        model.addAttribute("request", request);
        return "request/requestDetail";
    }

}
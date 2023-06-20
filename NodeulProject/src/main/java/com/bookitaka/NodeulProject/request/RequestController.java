package com.bookitaka.NodeulProject.request;

import com.bookitaka.NodeulProject.member.dto.MemberUpdateAdminDTO;
import com.bookitaka.NodeulProject.member.model.Member;
import com.bookitaka.NodeulProject.member.security.Token;
import com.bookitaka.NodeulProject.member.service.MemberService;
import com.bookitaka.NodeulProject.sheet.SheetRegDto;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

    // 도서 검색 open api
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

    // 활동지 요청 폼
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MEMBER')")
    public String requestForm(Model model,
                              HttpServletRequest request) {
        model.addAttribute("requestDto", new RequestDto());
        Member currentMember = memberService.whoami(request.getCookies(), Token.ACCESS_TOKEN);
        model.addAttribute("currentMember", currentMember);
        return "request/requestForm";
    }

    // 활동지 요청 폼 전송 POST
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

    // 내가 보낸 요청 목록 (회원)
    @GetMapping("/myrequest")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MEMBER')")
    public String listMyRequest(Model model,
                                HttpServletRequest request,
                                @RequestParam(name = "page", defaultValue = "0") int page) {
        Member currentMember = memberService.whoami(request.getCookies(), Token.ACCESS_TOKEN);
        model.addAttribute("currentMember", currentMember);

        int size = 3;
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "requestRegdate"));

        Page<Request> myRequest = requestService.getMyRequest(currentMember, pageable);
        model.addAttribute("myRequest", myRequest);
        return "request/myRequest";
    }

    // 받은 요청 목록 (관리자)
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

    // 요청 상세보기
    @GetMapping("/detail/{requestNo}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MEMBER')")
    public String requestDetail(@PathVariable Long requestNo, Model model){
        Request request = requestService.getOneRequest(requestNo).orElse(null);
        log.info("requestDetail request = {}", request);
        model.addAttribute("request", request);
        return "request/requestDetail";
    }

    // 답변 상태 변경
    @PutMapping("/changestatus/{requestNo}/{requestIsdone}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "The user doesn't exist"), //
            @ApiResponse(code = 500, message = "Member edit failed")})
    public ResponseEntity<?> changestatus(@PathVariable Long requestNo,
                                          @PathVariable int requestIsdone) {
        log.info("================================ RequestController : changestatus");
        log.info("requestIsdone = {}", requestIsdone);
        if(requestService.changestatus(requestNo, requestIsdone)) {
            // 수정 성공시
            return ResponseEntity.ok().build();
        } else {
            // 수정 실패 시
            return ResponseEntity.internalServerError().build();
        }
    }


}
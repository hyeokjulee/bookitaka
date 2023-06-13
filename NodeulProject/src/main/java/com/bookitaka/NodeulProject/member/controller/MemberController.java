package com.bookitaka.NodeulProject.member.controller;

import com.bookitaka.NodeulProject.member.dto.MemberDataDTO;
import com.bookitaka.NodeulProject.member.dto.MemberResponseDTO;
import com.bookitaka.NodeulProject.member.model.Member;
import com.bookitaka.NodeulProject.member.security.Token;
import com.bookitaka.NodeulProject.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final ModelMapper modelMapper;

    @GetMapping("/login")
    public String login(HttpServletRequest request) {
        log.info("=====================MemberController - login");
        if (memberService.whoami(request.getCookies(), Token.ACCESS_TOKEN) == null) {
            return "member/login/login";
        } else {
            return "index";
        }
    }

    @GetMapping("/signup")
    public String signup(HttpServletRequest request, Model model) {
        log.info("=====================MemberController - signup");
        model.addAttribute("member", new MemberDataDTO());
        if (memberService.whoami(request.getCookies(), Token.ACCESS_TOKEN) == null) {
            return "member/login/signup";
        } else {
            return "index";
        }
    }

    @GetMapping("/test")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MEMBER')")
    public String test() {
        log.info("=====================MemberController - test");
        return "member/authPage";
    }

    @GetMapping("/info")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MEMBER')")
    public String edit(Model model, HttpServletRequest request) {
        log.info("=====================MemberController - edit");
        MemberResponseDTO userResponseDTO = modelMapper.map(memberService.whoami(request.getCookies(), Token.ACCESS_TOKEN), MemberResponseDTO.class);
        model.addAttribute("member", userResponseDTO);
        return "member/my-info";
    }

    @GetMapping("/editAdmin/{memberEmail}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editAdmin(Model model, @PathVariable String memberEmail) {
        log.info("=====================MemberController - editAdmin");
        Member memberByEmail = memberService.search(memberEmail);
        MemberResponseDTO userResponseDTO = modelMapper.map(memberByEmail, MemberResponseDTO.class);
        model.addAttribute("member", userResponseDTO);
        return "member/admin/editAdmin";
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String listMembers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String method,
            @RequestParam(defaultValue = "") String keyword,
            HttpServletRequest request,
            Model model) {
        log.info(method);
        log.info(keyword);
        int pageSize = 10;
        PageRequest pageable = PageRequest.of(page, size);
        Page<Member> memberPage = memberService.getAllMembersPaging(pageable, keyword, method, request.getCookies());
        model.addAttribute("members", memberPage.getContent());
        model.addAttribute("totalPages", memberPage.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("method", method);
        model.addAttribute("keyword", keyword);

        int currentGroup = page / pageSize;
        int startPage = currentGroup * pageSize;
        int totalPages = memberPage.getTotalPages();
        int endPage = Math.min(startPage + pageSize, memberPage.getTotalPages()) - 1;

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        // 이전 그룹의 첫 번째 페이지로 이동
        int previousGroupStartPage = (currentGroup == 0) ? 0 : (currentGroup - 1) * pageSize;
        model.addAttribute("previousGroupStartPage", previousGroupStartPage);

        // 다음 그룹의 첫 번째 페이지로 이동
        int nextGroupStartPage = (currentGroup + 1) * pageSize;

        if (totalPages % 10 == 0 && startPage == totalPages - 10) {
            nextGroupStartPage = endPage;
        } else if (nextGroupStartPage > endPage + 1) {
            nextGroupStartPage = endPage;
        }
        model.addAttribute("nextGroupStartPage", nextGroupStartPage);
        return "member/admin/list";
    }

    @GetMapping("/find-email")
    public String findId(HttpServletRequest request) {
        if (memberService.whoami(request.getCookies(), Token.ACCESS_TOKEN) == null) {
            return "member/login/findEmail";
        } else {
            return "index";
        }
    }

    @GetMapping("/find-pw")
    public String findPw(HttpServletRequest request) {
        if (memberService.whoami(request.getCookies(), Token.ACCESS_TOKEN) == null) {
            return "member/login/findPw";
        } else {
            return "index";
        }
    }

    @GetMapping("/changePw")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MEMBER')")
    public String changePw() {
        return "member/changePw";
    }

    // 회원 상세 보기 (관리자)
    @GetMapping(value = "/{memberEmail}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String detailAdmin(@PathVariable String memberEmail, Model model) {
        log.info("================================Members : detailAdmin");
        model.addAttribute("member", memberService.search(memberEmail));
        return "member/detail";
    }
}

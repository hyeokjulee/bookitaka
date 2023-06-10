package com.bookitaka.NodeulProject.member.controller;

import com.bookitaka.NodeulProject.member.dto.MemberDataDTO;
import com.bookitaka.NodeulProject.member.dto.MemberResponseDTO;
import com.bookitaka.NodeulProject.member.model.Member;
import com.bookitaka.NodeulProject.member.security.Token;
import com.bookitaka.NodeulProject.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String list(Model model) {
        List<Member> members = memberService.getAllMembers();
//        model.addAttribute("members", members);
        List<Member> filteredMembers = members.stream()
                .filter(member -> member.getMemberRole().equals("ROLE_MEMBER"))
                .collect(Collectors.toList());
        model.addAttribute("members", filteredMembers);
        return "member/admin/list";
    }

    @GetMapping("/findEmail")
    public String findId(HttpServletRequest request) {
        if (memberService.whoami(request.getCookies(), Token.ACCESS_TOKEN) == null) {
            return "member/login/findEmail";
        } else {
            return "index";
        }
    }

    @GetMapping("/findPw")
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
        model.addAttribute("role", "admin");
        return "member/detail";
    }

    // 내 정보 보기 (회원)
    @GetMapping(value = "/me")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MEMBER')")
    public String detail(HttpServletRequest request, Model model) {
        log.info("================================Members : detail");
        model.addAttribute("member",memberService.whoami(request.getCookies(), Token.ACCESS_TOKEN));
        model.addAttribute("role", "member");
        return "member/detail";
    }

}

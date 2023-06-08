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
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
            return "login/login";
        } else {
            return "index";
        }
    }

    @GetMapping("/signup")
    public String signup(HttpServletRequest request, Model model) {
        log.info("=====================MemberController - signup");
        model.addAttribute("member", new MemberDataDTO());
        if (memberService.whoami(request.getCookies(), Token.ACCESS_TOKEN) == null) {
            return "login/signup";
        } else {
            return "index";
        }
    }

    @GetMapping("/test")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MEMBER')")
    public String test() {
        log.info("=====================MemberController - test");
        return "login/authPage";
    }

    @GetMapping("/edit")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MEMBER')")
    public String edit(Model model, HttpServletRequest request) {
        log.info("=====================MemberController - edit");
        MemberResponseDTO userResponseDTO = modelMapper.map(memberService.whoami(request.getCookies(), Token.ACCESS_TOKEN), MemberResponseDTO.class);
        model.addAttribute("member", userResponseDTO);
        return "login/edit";
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String list(Model model) {
        List<Member> members = memberService.getAllMembers();
        model.addAttribute("members", members);
        return "login/list";
    }

    @GetMapping("/findEmail")
    public String findId(HttpServletRequest request) {
        if (memberService.whoami(request.getCookies(), Token.ACCESS_TOKEN) == null) {
            return "login/findEmail";
        } else {
            return "index";
        }
    }

    @GetMapping("/findPw")
    public String findPw(HttpServletRequest request) {
        if (memberService.whoami(request.getCookies(), Token.ACCESS_TOKEN) == null) {
            return "login/findPw";
        } else {
            return "index";
        }
    }

    @GetMapping("/changePw")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MEMBER')")
    public String changePw() {
        return "login/changePw";
    }
}

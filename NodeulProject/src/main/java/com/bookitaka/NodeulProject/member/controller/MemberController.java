package com.bookitaka.NodeulProject.member.controller;

import com.bookitaka.NodeulProject.member.dto.UserResponseDTO;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public String login() {
        return "login/login";
    }

    @GetMapping("/test")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MEMBER')")
    public String test() {
        log.info("=====================test");
        return "login/authPage";
    }

    @GetMapping("/edit")
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    public String edit(Model model, HttpServletRequest request) {
        UserResponseDTO userResponseDTO = modelMapper.map(memberService.whoami(request.getCookies(), Token.ACCESS_TOKEN), UserResponseDTO.class);
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
    public String findId() { return "login/findEmail"; }

    @GetMapping("/findEmailResult")
    public String findEmailResult(Model model) {
        List<String> findResult = (List<String>) model.getAttribute("findResult");
        model.addAttribute("findResult", findResult);
        return "login/findEmailResult";
    }


    @GetMapping("/findPw")
    public String findPw() { return "login/findPw"; }

    @GetMapping("/signup")
    public String signup() { return "login/signup"; }

    @GetMapping("/changePw")
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    public String changePw() { return "login/changePw"; }
}

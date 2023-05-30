package com.bookitaka.NodeulProject.member.controller;

import com.bookitaka.NodeulProject.member.dto.UserResponseDTO;
import com.bookitaka.NodeulProject.member.model.Member;
import com.bookitaka.NodeulProject.member.repository.MemberRepository;
import com.bookitaka.NodeulProject.member.service.MemberService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sun.tools.attach.VirtualMachine.list;

@Slf4j
@Controller
@RequestMapping("/members")
public class MemberController {
    private MemberRepository memberRepository;
    private MemberService memberService;
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
    public String edit() {
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
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    public String findId() { return "login/findEmail"; }
    @GetMapping("/findPw")
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    public String findPw() { return "login/findPw"; }
    @GetMapping("/signup")
    public String signup() { return "login/signup"; }
    @GetMapping("/changePw")
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    public String changePw() { return "login/changePw"; }


}

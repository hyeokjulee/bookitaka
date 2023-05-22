package com.bookitaka.NodeulProject.member.bstest.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/members")
public class MemberController {

    @GetMapping("/login")
    public String login() {
        return "login/login";
    }

    @GetMapping("/test")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public String test() {
        return "login/authPage";
    }
}

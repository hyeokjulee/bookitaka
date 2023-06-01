package com.bookitaka.NodeulProject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainController {

    @GetMapping("/index")
    public String indexPage() {
        return "index";
    }

    @GetMapping("/pricing")
    public String pricingPage() {
        return "pricing";
    }

    @GetMapping("/web-development")
    public String webDevelopmentPage() {
        return "web-development";
    }

    @GetMapping("/user-research")
    public String userResearchPage() {
        return "user-research";
    }
}

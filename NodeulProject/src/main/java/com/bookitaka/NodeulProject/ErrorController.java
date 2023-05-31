package com.bookitaka.NodeulProject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController {

    @RequestMapping("/error/403")
    public String handle404Error() {
        return "error/403";
    }
}
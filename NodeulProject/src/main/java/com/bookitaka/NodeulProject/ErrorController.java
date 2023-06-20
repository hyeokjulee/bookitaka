package com.bookitaka.NodeulProject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController {

    @RequestMapping("/error/403")
    public String handle403Error() {
        return "error/403";
    }

    @RequestMapping("/error/404")
    public String handle404Error() {
        return "error/404";
    }

    @RequestMapping("/error/500")
    public String handleServerError() {
        // 500 번대 오류 처리 로직을 작성합니다.
        return "error/500"; // 500 번대 오류 페이지로 이동합니다.
    }
}
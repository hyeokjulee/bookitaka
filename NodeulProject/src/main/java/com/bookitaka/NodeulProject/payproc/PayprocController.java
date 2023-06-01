package com.bookitaka.NodeulProject.payproc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/payproc")
@RequiredArgsConstructor
@Slf4j
public class PayprocController {

    @GetMapping("/paying")
    public String paying() {
        return "/payproc/paying";
    }

}

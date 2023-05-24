package com.bookitaka.NodeulProject.manual.controller;

import com.bookitaka.NodeulProject.manual.domain.entity.Manual;
import com.bookitaka.NodeulProject.manual.service.ManualService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class ManualController {

    @Autowired
    private ManualService manualService;

    @GetMapping("/manual/write")
    public String manualWriteForm(){

        return "manual/manualWrite.html";
    }

    @PostMapping("/manual/writepro")
    public String manualWritepro(Manual manual){

        manualService.manualwrite(manual);

        return "";

    }
}

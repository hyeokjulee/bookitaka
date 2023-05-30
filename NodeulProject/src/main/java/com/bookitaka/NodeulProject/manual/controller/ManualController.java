package com.bookitaka.NodeulProject.manual.controller;

import com.bookitaka.NodeulProject.manual.dto.ManualDto;
import com.bookitaka.NodeulProject.manual.service.ManualService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/manual")
public class ManualController {

    private ManualService manualService;

    @GetMapping("/list")
    public String list(Model model){
        List<ManualDto> manualList = manualService.getManualList();

        model.addAttribute("manualList",manualList);
        return "manual/list.html";
    }

    @GetMapping("/post")
    public String write(){
        return "manual/write.html";
    }

    @PostMapping("/post")
    public String write(ManualDto manualDto){
        manualService.saveManual(manualDto);
        return "redirect:/manual/list";
    }

    @GetMapping("/post/{manualNo}")
    public String detail(@PathVariable("manualNo") Integer manualNo, Model model){
        ManualDto manualDto = manualService.getManual(manualNo);
        model.addAttribute("manualDto",manualDto);

        return "manual/detail.html";
    }

    @GetMapping("/post/edit/{manualNo}")
    public String edit(@PathVariable("manualNo") Integer manualNo, Model model){
        ManualDto manualDto = manualService.getManual(manualNo);
        model.addAttribute("manualDto",manualDto);

        return "manual/update.html";
    }

    @PutMapping("/post/edit/{manualNo}")
    public String update(ManualDto manualDto){
        manualService.saveManual(manualDto);
        return "redirect:/manual/list";
    }

    @DeleteMapping("/post/{manualNo}")
    public String delete(@PathVariable("manualNo") Integer manualNo){
        manualService.deleteManual(manualNo);

        return "redirect:/manual/list";
    }
}

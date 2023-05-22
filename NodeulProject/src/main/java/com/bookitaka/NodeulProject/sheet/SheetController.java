package com.bookitaka.NodeulProject.sheet;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sheet")
@RequiredArgsConstructor
public class SheetController {

    private final SheetRepository sheetRepository;

    @GetMapping("/add")
    public String sheetAddForm(Model model) {
        model.addAttribute("sheetRegDto", new SheetRegDto());
        return "/sheet/sheetAddForm";
    }


}

package com.bookitaka.NodeulProject;

import com.bookitaka.NodeulProject.sheet.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
@Slf4j
public class MainController {

    private final SheetService sheetService;

    @GetMapping()
    public String indexPage(Model model) {
        SheetCri newCri = new SheetCri(1, 4, SearchTypes.TITLE, "", SortCries.NEWEST);
        List<Sheet> newSheets = sheetService.getAllSheets("", "", newCri);
        log.info("newSheets = {}", newSheets);

        SheetCri buyCri = new SheetCri(1, 4, SearchTypes.TITLE, "", SortCries.BUYCNT);
        List<Sheet> topSheets = sheetService.getAllSheets("", "", buyCri);
        log.info("topSheets = {}", topSheets);

        model.addAttribute("newSheets", newSheets);
        model.addAttribute("topSheets", topSheets);
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

package com.bookitaka.NodeulProject;

import com.bookitaka.NodeulProject.manual.ManualDto;
import com.bookitaka.NodeulProject.manual.ManualService;
import com.bookitaka.NodeulProject.notice.NoticeDto;
import com.bookitaka.NodeulProject.notice.NoticeService;
import com.bookitaka.NodeulProject.sheet.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    private final ManualService manualService;
    private final NoticeService noticeService;

    @GetMapping()
    public String indexPage(Model model) {
        SheetCri newCri = new SheetCri(1, 4, SearchTypes.TITLE, "", SortCries.NEWEST);
        List<Sheet> newSheets = sheetService.getAllSheets("", "", newCri);
        log.info("newSheets = {}", newSheets);

        SheetCri buyCri = new SheetCri(1, 4, SearchTypes.TITLE, "", SortCries.BUYCNT);
        List<Sheet> topSheets = sheetService.getAllSheets("", "", buyCri);
        log.info("topSheets = {}", topSheets);

        Page<ManualDto> manualList = manualService.getManualList(PageRequest.of(0, 5 , Sort.by("manualNo").descending()),null);

        Page<NoticeDto> noticeList = noticeService.getNoticeList(PageRequest.of(0, 5, Sort.by("noticeRegdate").descending()),null);
        Long sheetCnt = sheetService.getSheetCnt("", "", SearchTypes.TITLE, "");

        model.addAttribute("newSheets", newSheets);
        model.addAttribute("topSheets", topSheets);
        model.addAttribute("sheetCnt", sheetCnt);
        model.addAttribute("manuals",manualList);
        model.addAttribute("notices", noticeList);

        return "main";
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

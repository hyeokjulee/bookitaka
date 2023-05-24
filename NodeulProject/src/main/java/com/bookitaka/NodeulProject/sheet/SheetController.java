package com.bookitaka.NodeulProject.sheet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/sheet")
@RequiredArgsConstructor
@Slf4j
public class SheetController {

    private final SheetService sheetService;

    @GetMapping("/add")
    public String sheetAddForm(Model model) {
        model.addAttribute("sheetRegDto", new SheetRegDto());
        model.addAttribute("ageGroup", sheetService.getAllSheetAgeGroup());
        model.addAttribute("genre", sheetService.getAllSheetGenre());
        return "/sheet/sheetAddForm";
    }

    @PostMapping("/add")
    public String sheetAdd(@ModelAttribute SheetRegDto sheetRegDto,
                           @RequestParam("sheetBookImg") MultipartFile sheetBookImg,
                           @RequestParam("sheetFile") MultipartFile sheetFile) throws IOException {

        UploadFile uploadBookImg = null;
        UploadFile uploadSheetFile = null;

        // 파일 업로드 처리 로직
        if (!sheetBookImg.isEmpty()) {
            // 업로드된 파일 저장
            uploadBookImg = sheetService.storeBookImg(sheetBookImg);
        }
        if (!sheetFile.isEmpty()) {
            uploadSheetFile = sheetService.storeSheetFile(sheetFile);
        }
        // SheetRegDto를 사용한 비즈니스 로직 처리
        sheetService.registerSheet(sheetRegDto, uploadBookImg, uploadSheetFile);

        return "sheet/success";

//        return "redirect:/sheet?sheetNo" + sheetNo;
    }


}

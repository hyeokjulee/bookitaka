package com.bookitaka.NodeulProject.sheet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/sheet")
@RequiredArgsConstructor
@Slf4j
public class SheetController {

    private final SheetService sheetService;

    @Value("${file.bookImg.dir}")
    private String bookImgDir;

    @Value("${file.sheetFile.dir}")
    private String sheetFileDir;

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
        Sheet sheet = sheetService.registerSheet(sheetRegDto, uploadBookImg, uploadSheetFile);

//        return "sheet/success";

        return "redirect:/sheet/" + sheet.getSheetNo();
    }

    @GetMapping("/list")
    public String sheetList(@RequestParam(name = "page", defaultValue = "1") int page,
                            @RequestParam(name = "amount", defaultValue = "10") int amount,
                            @RequestParam(name = "searchType", defaultValue = SearchTypes.TITLE) String searchType,
                            @RequestParam(name = "search", required = false) String searchWord,
                            Model model) {

        SheetCri cri = new SheetCri(page, amount, searchType, searchWord);

        model.addAttribute("sheet", sheetService.getAllSheets(cri));
        return "sheet/sheetList";
    }


    @GetMapping("/{sheetNo}")
    public String sheetDetail(@PathVariable int sheetNo, Model model) {
        Sheet sheet = sheetService.getSheet(sheetNo);
        model.addAttribute("sheet", sheet);
        model.addAttribute("bookImgSrc", bookImgDir + sheet.getSheetBookimguuid() + sheet.getSheetBookimgname());
        model.addAttribute("sheetFileSrc", sheetFileDir + sheet.getSheetFileuuid() + sheet.getSheetFilename());
        return "sheet/sheetDetail";
    }

    @ResponseBody
    @GetMapping("/bookImg/{imgName}")
    public Resource downloadBookImg(@PathVariable String imgName) throws MalformedURLException {
        return new UrlResource("file:" + bookImgDir + imgName);
    }

    @GetMapping("/sheetFile/{fileUuid}")
    public ResponseEntity<Resource> downloadSheetFile(@PathVariable String fileUuid)
            throws MalformedURLException {

        String fileName = sheetService.getFileNameByUuid(fileUuid);
        String fullFileName = fileUuid + fileName;
        UrlResource resource = new UrlResource("file:" + sheetFileDir + fullFileName);
        log.info("uploadFileName={}", fullFileName);
        String encodedUploadFileName = UriUtils.encode(fileName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }

    @GetMapping("/{sheetNo}/mod")
    public String modBookImgForm(@PathVariable int sheetNo, Model model) {
        //만든사람 or 관리자인지 인증 필요
        Sheet sheet = sheetService.getSheet(sheetNo);
        model.addAttribute("sheet", sheet);
        model.addAttribute("bookImgSrc", bookImgDir + sheet.getSheetBookimguuid() + sheet.getSheetBookimgname());
        model.addAttribute("sheetFileSrc", sheetFileDir + sheet.getSheetFileuuid() + sheet.getSheetFilename());
        return "sheet/sheetDetail";

    }

    @DeleteMapping("/{sheetNo}")
    public ResponseEntity<String> deleteSheet(@PathVariable int sheetNo){
        boolean result = sheetService.removeSheet(sheetNo);
        // 삭제 처리 완료 후, 응답을 보낸다
        if (result) {
            return ResponseEntity.ok("성공적으로 삭제되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("삭제에 실패했습니다.");
        }

    }

}

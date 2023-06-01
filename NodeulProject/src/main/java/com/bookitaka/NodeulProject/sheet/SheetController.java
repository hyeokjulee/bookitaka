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
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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
    public String sheetAdd(@Validated @ModelAttribute SheetRegDto sheetRegDto,
                           BindingResult bindingResult,
                           @RequestParam("sheetBookImg") MultipartFile sheetBookImg,
                           @RequestParam("sheetFile") MultipartFile sheetFile,
                           Model model) throws IOException {


        if (sheetFile == null || sheetFile.isEmpty()) {
            bindingResult.reject("noSheetFile", "파일을 업로드해주세요.");
        }

//        if (!sheetRegDto.getSheetGenreName().equals("독서토론논제은행") && sheetBookImg == null) {
//            bindingResult.reject("noAgeGroup", "연령대를 선택해주세요.");
//        }


        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);

            model.addAttribute("ageGroup", sheetService.getAllSheetAgeGroup());
            model.addAttribute("genre", sheetService.getAllSheetGenre());
            return "/sheet/sheetAddForm";
        }

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

        return "redirect:/sheet/" + sheet.getSheetNo();
    }

    //테스트용 데이터 30개 넣기
//    @PostMapping("/add")
//    public String sheetAdd(@ModelAttribute SheetRegDto sheetRegDto,
//                           @RequestParam("sheetBookImg") MultipartFile sheetBookImg,
//                           @RequestParam("sheetFile") MultipartFile sheetFile) throws IOException {
//
//        for (int i = 0; i < 30; i++) {
//
//        UploadFile uploadBookImg = null;
//        UploadFile uploadSheetFile = null;
//
//            // 파일 업로드 처리 로직
//        if (!sheetBookImg.isEmpty()) {
//            // 업로드된 파일 저장
//            uploadBookImg = sheetService.storeBookImg(sheetBookImg);
//        }
//        if (!sheetFile.isEmpty()) {
//            uploadSheetFile = sheetService.storeSheetFile(sheetFile);
//        }
//
//        // SheetRegDto를 사용한 비즈니스 로직 처리
//        sheetRegDto.setSheetBooktitle(sheetRegDto.getSheetBooktitle().substring(0, sheetRegDto.getSheetBooktitle().length() - 1) + i);
//        sheetRegDto.setSheetBookauthor(sheetRegDto.getSheetBookauthor().substring(0, sheetRegDto.getSheetBookauthor().length() - 1) + i);
//        sheetRegDto.setSheetBookpublisher(sheetRegDto.getSheetBookpublisher().substring(0, sheetRegDto.getSheetBookpublisher().length() - 1) + i);
//
//        sheetService.registerSheet(sheetRegDto, uploadBookImg, uploadSheetFile);
//        }
//
//        return "redirect:/sheet/list";
//    }

    @GetMapping("/list")
    public String sheetList(@RequestParam(name = "genre", defaultValue = "") String genre,
                            @RequestParam(name = "ageGroup", defaultValue = "") String ageGroup,
                            @RequestParam(name = "pageNum", defaultValue = "1") int page,
                            @RequestParam(name = "amount", defaultValue = "10") int amount,
                            @RequestParam(name = "searchType", defaultValue = SearchTypes.TITLE) String searchType,
                            @RequestParam(name = "searchWord", defaultValue = "") String searchWord,
                            @RequestParam(name = "sort", defaultValue = SortCries.NEWEST) String sort,
                            Model model) {

        SheetCri cri = new SheetCri(page, amount, searchType, searchWord, sort);
        int totalNum = Math.toIntExact(sheetService.getSheetCnt(genre, ageGroup, searchType, searchWord));

        log.info("controller genre = {}", genre);
        log.info("controller ageGroup = {}", ageGroup);
        model.addAttribute("sheetList", sheetService.getAllSheets(genre, ageGroup, cri));
        model.addAttribute("pageInfo", new SheetPageInfo(cri, totalNum));
        model.addAttribute("cri", cri);
        model.addAttribute("genre", genre);
        model.addAttribute("ageGroup", ageGroup);

        return "sheet/sheetList";
    }

    @GetMapping("/{sheetNo}")
    public String sheetDetail(@PathVariable int sheetNo,
                              @RequestParam(name = "genre", defaultValue = "") String genre,
                              @RequestParam(name = "ageGroup", defaultValue = "") String ageGroup,
                              @RequestParam(name = "pageNum", defaultValue = "1") int page,
                              @RequestParam(name = "amount", defaultValue = "10") int amount,
                              @RequestParam(name = "searchType", defaultValue = SearchTypes.TITLE) String searchType,
                              @RequestParam(name = "searchWord", defaultValue = "") String searchWord,
                              @RequestParam(name = "sort", defaultValue = SortCries.NEWEST) String sort,
                              Model model) {
        Sheet sheet = sheetService.getSheet(sheetNo);
        SheetCri cri = new SheetCri(page, amount, searchType, searchWord, sort);

        model.addAttribute("sheet", sheet);
        model.addAttribute("cri", cri);
        model.addAttribute("genre", genre);
        model.addAttribute("ageGroup", ageGroup);

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

//    @GetMapping("/{sheetNo}/mod")
//    public String modBookImgForm(@PathVariable int sheetNo, Model model) {
//        //만든사람 or 관리자인지 인증 필요
//        //아니면 자세히보기 등 다른페이지로 리다이렉트
//        Sheet sheet = sheetService.getSheet(sheetNo);
//        model.addAttribute("sheet", sheet);
//        model.addAttribute("bookImgSrc", bookImgDir + sheet.getSheetBookimguuid() + sheet.getSheetBookimgname());
//        model.addAttribute("sheetFileSrc", sheetFileDir + sheet.getSheetFileuuid() + sheet.getSheetFilename());
//        return "sheet/sheetDetail";
//    }

    @GetMapping("/{sheetNo}/mod")
    public String sheetModForm(@PathVariable int sheetNo, Model model) {


        Sheet sheet = sheetService.getSheet(sheetNo);

        SheetUpdateDto updateDto = new SheetUpdateDto();
        updateDto.setSheetBooktitle(sheet.getSheetBooktitle());
        updateDto.setSheetBookauthor(sheet.getSheetBookauthor());
        updateDto.setSheetBookpublisher(sheet.getSheetBookpublisher());
        updateDto.setSheetBookisbn(sheet.getSheetBookisbn());
        updateDto.setSheetPrice(sheet.getSheetPrice());
        updateDto.setSheetBookimguuid(sheet.getSheetBookimguuid());
        updateDto.setSheetBookimgname(sheet.getSheetBookimgname());
        updateDto.setSheetFileuuid(sheet.getSheetFileuuid());
        updateDto.setSheetFilename(sheet.getSheetFilename());
        updateDto.setSheetGenreName(sheet.getSheetGenre().getSheetGenreName());
        updateDto.setSheetAgegroupName(sheet.getSheetAgegroup().getSheetAgegroupName());
        updateDto.setSheetContent(sheet.getSheetContent());

        model.addAttribute("sheetUpdataDto", updateDto);
        model.addAttribute("ageGroup", sheetService.getAllSheetAgeGroup());
        model.addAttribute("genre", sheetService.getAllSheetGenre());

        return "sheet/sheetModForm";
    }

    @PostMapping("/{sheetNo}/mod")
    public String sheetMod(@PathVariable int sheetNo,
                           @Validated @ModelAttribute("sheetUpdateDto") SheetUpdateDto sheetUpdateDto,
                           BindingResult bindingResult,
                           @RequestParam("sheetBookImg") MultipartFile sheetBookImg,
                           @RequestParam("sheetFile") MultipartFile sheetFile,
                           Model model) throws IOException {


        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);

            model.addAttribute("ageGroup", sheetService.getAllSheetAgeGroup());
            model.addAttribute("genre", sheetService.getAllSheetGenre());
            return "sheet/sheetModForm";
        }

        UploadFile uploadBookImg = null;
        UploadFile uploadSheetFile = null;

        // 파일 업로드 처리 로직
        if (!sheetBookImg.isEmpty()) {
            // 업로드된 파일 삭제 후 저장
            sheetService.removeStoredFile(bookImgDir + sheetUpdateDto.getSheetBookimguuid() + sheetUpdateDto.getSheetBookimgname());
            uploadBookImg = sheetService.storeBookImg(sheetBookImg);
            sheetUpdateDto.setSheetBookimguuid(uploadBookImg.uuid);
        }
        if (!sheetFile.isEmpty()) {
            sheetService.removeStoredFile(sheetFileDir + sheetUpdateDto.getSheetFileuuid() + sheetUpdateDto.getSheetFilename());
            uploadSheetFile = sheetService.storeSheetFile(sheetFile);
            sheetUpdateDto.setSheetFileuuid(uploadSheetFile.uuid);
        }
        // SheetRegDto를 사용한 비즈니스 로직 처리

        sheetService.modifySheet(sheetNo, sheetUpdateDto);

        return "redirect:/sheet/" + sheetNo;
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
package com.bookitaka.NodeulProject.sheet;

import com.bookitaka.NodeulProject.member.model.Member;
import com.bookitaka.NodeulProject.member.security.Token;
import com.bookitaka.NodeulProject.member.service.MemberService;
import com.bookitaka.NodeulProject.mysheet.Mysheet;
import com.bookitaka.NodeulProject.mysheet.MysheetCri;
import com.bookitaka.NodeulProject.mysheet.MysheetPageInfo;
import com.bookitaka.NodeulProject.mysheet.MysheetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequestMapping("/sheet")
@RequiredArgsConstructor
@Slf4j
public class SheetController {

    private final SheetService sheetService;
    private final MysheetService mysheetService;
    private final MemberService memberService;
//    private final RequestService requestService;

    @Value("${file.bookImg.dir}")
    private String bookImgDir;

    @Value("${file.sheetFile.dir}")
    private String sheetFileDir;

    @Value("${file.preview.dir}")
    private String previewDir;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/add")
    public String sheetAddForm(Model model) {
        model.addAttribute("sheetRegDto", new SheetRegDto());
        model.addAttribute("ageGroup", sheetService.getAllSheetAgeGroup());
        model.addAttribute("genre", sheetService.getAllSheetGenre());
        return "sheet/sheetAddForm";
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    public String sheetAdd(@Validated @ModelAttribute SheetRegDto sheetRegDto,
                           BindingResult bindingResult,
                           @RequestParam("sheetBookImg") MultipartFile sheetBookImg,
                           @RequestParam("sheetFile") MultipartFile sheetFile,
                           Model model) throws IOException {


        if (sheetFile == null || sheetFile.isEmpty()) {
            bindingResult.reject("noSheetFile", "파일을 업로드해주세요.");
        }


        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);

            model.addAttribute("ageGroup", sheetService.getAllSheetAgeGroup());
            model.addAttribute("genre", sheetService.getAllSheetGenre());
            return "sheet/sheetAddForm";
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

    @ResponseBody
    @GetMapping("/listapi")
    public List<Sheet> sheetListapi(@RequestParam(name = "genre", defaultValue = "") String genre,
                                    @RequestParam(name = "ageGroup", defaultValue = "") String ageGroup,
                                    @RequestParam(name = "pageNum", defaultValue = "1") int page,
                                    @RequestParam(name = "amount", defaultValue = "10") int amount,
                                    @RequestParam(name = "searchType", defaultValue = SearchTypes.TITLE) String searchType,
                                    @RequestParam(name = "searchWord", defaultValue = "") String searchWord,
                                    @RequestParam(name = "sort", defaultValue = SortCries.NEWEST) String sort) {
        SheetCri cri = new SheetCri(page, amount, searchType, searchWord, sort);
        return sheetService.getAllSheets(genre, ageGroup, cri);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MEMBER')")
    @GetMapping("/mysheet")
    public String mySheetList(HttpServletRequest request,
                              @RequestParam(name = "pageNum", defaultValue = "1") int page,
                              @RequestParam(name = "amount", defaultValue = "10") int amount,
                              @RequestParam(name = "searchType", defaultValue = SearchTypes.TITLE) String searchType,
                              @RequestParam(name = "searchWord", defaultValue = "") String searchWord,
                              Model model) {


        MysheetCri cri = new MysheetCri(page, amount, searchType, searchWord);
        Member member = memberService.whoami(request.getCookies(), Token.ACCESS_TOKEN);

        int totalNum = Math.toIntExact(mysheetService.getMySheetCnt(searchType, searchWord, member));
        model.addAttribute("sheetList", mysheetService.getAllMysheetByMember(cri, member));
        model.addAttribute("pageInfo", new MysheetPageInfo(cri, totalNum));
        model.addAttribute("cri", cri);

        return "sheet/mysheet";

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

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MEMBER')")
    @GetMapping("/sheetFile/{fileUuid}")
    public ResponseEntity<Resource> downloadSheetFile(HttpServletRequest request,
                                                      @PathVariable String fileUuid)
            throws MalformedURLException {

        //인증 과정
        //누구인지 찾기
        Member member = memberService.whoami(request.getCookies(), Token.ACCESS_TOKEN);

        log.info("downloadController Fileuuid = {}", fileUuid);

        //관리자가 아닐때만 조건 체크(관리자면 무조건 다운가능)
        if (!member.getMemberRole().equals("ROLE_ADMIN")) {
            //그 사람 mysheet기록, fileUuid로 찾기
            Mysheet mysheet = mysheetService.canIDownloadSheet(fileUuid, member);
            if (mysheet == null) { //null이면 badrequest보내기.
                String errorMessage = "구입 내역이 없습니다";
                Resource errorResource = new ByteArrayResource(errorMessage.getBytes());
                return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(errorResource);
            }

            //아니면 날짜 체크하기
            if (!mysheetService.checkMySheetIsAvailable(mysheet)) {
                String errorMessage = "다운로드 기간이 만료된 상품입니다";
                Resource errorResource = new ByteArrayResource(errorMessage.getBytes());
                return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(errorResource);
            }
        }



        //안지났으면 프로세스 진행
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

    @GetMapping("/preview/{fileUuid}")
    public ResponseEntity<Resource> downloadPreviewFile(@PathVariable String fileUuid)
            throws MalformedURLException {

        String fileName = sheetService.getFileNameByUuid(fileUuid);
        String fullFileName = fileUuid + fileName;
        UrlResource resource = new UrlResource("file:" + previewDir + fullFileName);
        log.info("previewFileName={}", fullFileName);
        String encodedUploadFileName = UriUtils.encode(fileName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
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

        model.addAttribute("sheetUpdateDto", updateDto);
        model.addAttribute("ageGroup", sheetService.getAllSheetAgeGroup());
        model.addAttribute("genre", sheetService.getAllSheetGenre());

        return "sheet/sheetModForm";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{sheetNo}/mod")
    public String sheetMod(@PathVariable int sheetNo,
                           @Validated @ModelAttribute SheetUpdateDto sheetUpdateDto,
                           BindingResult bindingResult,
                           @RequestParam(value = "sheetBookImg", required = false) MultipartFile sheetBookImg,
                           @RequestParam(value = "sheetFile", required = false) MultipartFile sheetFile,
                           Model model) throws IOException {


        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);

            Sheet sheet = sheetService.getSheet(sheetNo);
            sheetUpdateDto.setSheetBookimguuid(sheet.getSheetBookimguuid());
            sheetUpdateDto.setSheetBookimgname(sheet.getSheetBookimgname());
            sheetUpdateDto.setSheetFileuuid(sheet.getSheetFileuuid());
            sheetUpdateDto.setSheetFilename(sheet.getSheetFilename());

            model.addAttribute("sheetUpdateDto", sheetUpdateDto);
            model.addAttribute("ageGroup", sheetService.getAllSheetAgeGroup());
            model.addAttribute("genre", sheetService.getAllSheetGenre());
            return "sheet/sheetModForm";
        }

        UploadFile uploadBookImg = null;
        UploadFile uploadSheetFile = null;

        log.info("sheetBookImg = {}", sheetBookImg.isEmpty());
        log.info("sheetFile= {}", sheetFile.isEmpty());

        // 파일 업로드 처리 로직
        if (!sheetBookImg.isEmpty()) {
            // 업로드된 파일 삭제 후 저장
            sheetService.removeStoredFile(bookImgDir + sheetUpdateDto.getSheetBookimguuid() + sheetUpdateDto.getSheetBookimgname());
            uploadBookImg = sheetService.storeBookImg(sheetBookImg);
            sheetUpdateDto.setSheetBookimguuid(uploadBookImg.uuid);
            sheetUpdateDto.setSheetBookimgname(uploadBookImg.fileName);
        }
        if (!sheetFile.isEmpty()) {
            sheetService.removeStoredFile(sheetFileDir + sheetUpdateDto.getSheetFileuuid() + sheetUpdateDto.getSheetFilename());
            uploadSheetFile = sheetService.storeSheetFile(sheetFile);
            sheetUpdateDto.setSheetFileuuid(uploadSheetFile.uuid);
            sheetUpdateDto.setSheetFilename(uploadSheetFile.fileName);
        }
        // SheetRegDto를 사용한 비즈니스 로직 처리
        log.info("sheetUpdateDto = {}", sheetUpdateDto);

        sheetService.modifySheet(sheetNo, sheetUpdateDto);

        return "redirect:/sheet/" + sheetNo;
    }

    
    @DeleteMapping("/{sheetNo}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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

//    @GetMapping("/request")
//    public String requestForm() {
//
//        return "sheet/sheetRequest";
//    }
//    @PostMapping("/request")
//    public String requestProc() {
//
//        return "sheet/sheetRequest";
//    }
//
//    @GetMapping("/myrequest")
//    public String listMyRequest() {
//
//        return "sheet/sheetMyRequest";
//    }
//
//    @GetMapping("/requestlist")
//    public String listRequestForAdmin() {
//
//        return "sheet/sheetRequestList";
//    }

}
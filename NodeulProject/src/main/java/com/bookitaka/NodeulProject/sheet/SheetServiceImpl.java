package com.bookitaka.NodeulProject.sheet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SheetServiceImpl implements SheetService{

    private final SheetRepository sheetRepository;
    private final GenreRepository genreRepository;
    private final AgeGroupRepository ageGroupRepository;


    @Value("${file.bookImg.dir}")
    private String bookImgDir;

    @Value("${file.sheetFile.dir}")
    private String sheetFileDir;


    @Override
    public Sheet registerSheet(SheetRegDto sheetRegDto, UploadFile uploadBookImg, UploadFile uploadSheetFile) {
        Sheet sheet = new Sheet();
        log.info("sheetRegDto = {}", sheetRegDto);
        sheet.setSheetBooktitle(sheetRegDto.getSheetBooktitle());
        sheet.setSheetBookauthor(sheetRegDto.getSheetBookauthor());
        sheet.setSheetBookpublisher(sheetRegDto.getSheetBookpublisher());
        sheet.setSheetBookisbn(sheetRegDto.getSheetBookisbn());
        sheet.setSheetPrice(sheetRegDto.getSheetPrice());

        sheet.setSheetBookimguuid(uploadBookImg.uuid);
        sheet.setSheetBookimgname(uploadBookImg.fileName);

        sheet.setSheetFileuuid(uploadSheetFile.uuid);
        sheet.setSheetFilename(uploadSheetFile.fileName);

        sheet.setSheetGenre(genreRepository.findBySheetGenreName(sheetRegDto.getSheetGenreName()));
        sheet.setSheetAgegroup(ageGroupRepository.findBySheetAgegroupName(sheetRegDto.getSheetAgegroupName()));
        sheet.setSheetContent(sheetRegDto.getSheetContent());

        return sheetRepository.createSheet(sheet);
    }

    @Override
    public UploadFile storeBookImg(MultipartFile sheetBookImg) throws IOException {
        String bookImgName = sheetBookImg.getOriginalFilename();

        String uuid = UUID.randomUUID().toString();
        String bookImgFullPath = bookImgDir + uuid + bookImgName;
        log.info("bookImg 저장 fullPath={}", bookImgFullPath);
        sheetBookImg.transferTo(new File(bookImgFullPath));

        //테스트 데이터 넣기용
//        Files.copy(sheetBookImg.getInputStream(), Paths.get(bookImgFullPath), StandardCopyOption.REPLACE_EXISTING);

        return new UploadFile(uuid, bookImgName);
    }

    @Override
    public UploadFile storeSheetFile(MultipartFile sheetFile) throws IOException {

        String sheetFileName = sheetFile.getOriginalFilename();

        String uuid = UUID.randomUUID().toString();
        String sheetFileFullPath = sheetFileDir + uuid + sheetFileName;
        log.info("sheetFile 저장 fullPath = {}", sheetFileFullPath);
        sheetFile.transferTo(new File(sheetFileFullPath));
        //테스트 데이터 넣기용
//        Files.copy(sheetFile.getInputStream(), Paths.get(sheetFileFullPath), StandardCopyOption.REPLACE_EXISTING);

        return new UploadFile(uuid, sheetFileName);
    }

    @Override
    public Sheet getSheet(int sheetNo) {
        return sheetRepository.findSheetByNo(sheetNo).orElse(null);
    }

    @Override
    public Long getSheetCnt(String genre, String ageGroup, String searchType, String searchWord) {
        log.info("service cnt genre = {}", genre);
        log.info("service cnt ageGroup = {}", ageGroup);

        if (!genre.equals("")) {
            return sheetRepository.countSheetByGenre(genre, searchType, searchWord);
        } else if (!ageGroup.equals("")) {
            return sheetRepository.countSheetByAgeGroup(ageGroup, searchType, searchWord);
        } else {
            return sheetRepository.countSheet(searchType, searchWord);
        }

    }

    @Override
    public List<Sheet> getAllSheets(String genre, String ageGroup, SheetCri cri) {
        log.info("service sheet genre = {}", genre);
        log.info("service sheet ageGroup = {}", ageGroup);

        if (!genre.equals("")) {
            return sheetRepository.findAllSheetByGenre(genre, cri);
        } else if (!ageGroup.equals("")) {
            return sheetRepository.findAllSheetByAgeGroup(ageGroup, cri);
        } else {
            return sheetRepository.findAllSheet(cri);
        }

    }


    @Override
    public boolean modifySheet(int sheetNo, SheetUpdateDto sheetUpdateDto) {
        return sheetRepository.updateSheet(sheetNo, sheetUpdateDto);
    }

    @Override
    public boolean removeSheet(int sheetNo) {
        Sheet sheet = sheetRepository.findSheetByNo(sheetNo).orElse(null);
        boolean result1 = removeStoredFile(bookImgDir + sheet.getSheetBookimguuid() + sheet.getSheetBookimgname());
        boolean result2 = removeStoredFile(sheetFileDir + sheet.getSheetFileuuid() + sheet.getSheetFilename());

        if (result1 && result2) {
            return sheetRepository.deleteSheet(sheetNo);
        }
        return false;

    }

    private boolean removeStoredFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
            return true;
        }
        return false;
    }

    @Override
    public List<SheetGenre> getAllSheetGenre() {
        return genreRepository.findAll();
    }

    @Override
    public List<SheetAgegroup> getAllSheetAgeGroup() {
        return ageGroupRepository.findAll();
    }

    @Override
    public String getFileNameByUuid(String uuid) {
        return sheetRepository.findFileNameByUuid(uuid);
    }
}

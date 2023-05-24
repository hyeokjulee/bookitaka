package com.bookitaka.NodeulProject.sheet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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
        String BookImgFullPath = bookImgDir + uuid + bookImgName;
        log.info("bookImg 저장 fullPath={}", BookImgFullPath);
        sheetBookImg.transferTo(new File(BookImgFullPath));

        return new UploadFile(uuid, bookImgName);
    }

    @Override
    public UploadFile storeSheetFile(MultipartFile sheetFile) throws IOException {

        String sheetFileName = sheetFile.getOriginalFilename();

        String uuid = UUID.randomUUID().toString();
        String sheetFileFullPath = sheetFileDir + uuid + sheetFileName;
        log.info("sheetFile 저장 fullPath = {}", sheetFileFullPath);
        sheetFile.transferTo(new File(sheetFileFullPath));

        return new UploadFile(uuid, sheetFileName);
    }

    @Override
    public Sheet getSheet(int sheetNo) {
        return sheetRepository.findSheetByNo(sheetNo).orElse(null);
    }

    @Override
    public Long getSheetCnt() {
        return sheetRepository.countSheet();
    }

    @Override
    public List<Sheet> getAllSheetByGenre(String genreName, SheetCri cri) {
        return sheetRepository.findAllSheetByGenre(genreName, cri);
    }

    @Override
    public List<Sheet> getAllSheetByAgeGroup(String ageGroupName, SheetCri cri) {
        return sheetRepository.findAllSheetByAgeGroup(ageGroupName, cri);
    }

    @Override
    public boolean modifySheet(int sheetNo, SheetUpdateDto sheetUpdateDto) {
        return sheetRepository.updateSheet(sheetNo, sheetUpdateDto);
    }

    @Override
    public boolean removeSheet(int sheetNo) {
        return sheetRepository.deleteSheet(sheetNo);
    }

    @Override
    public List<SheetGenre> getAllSheetGenre() {
        return genreRepository.findAll();
    }

    @Override
    public List<SheetAgegroup> getAllSheetAgeGroup() {
        return ageGroupRepository.findAll();
    }
}

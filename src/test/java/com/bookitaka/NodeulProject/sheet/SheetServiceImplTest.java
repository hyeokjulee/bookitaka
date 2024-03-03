package com.bookitaka.NodeulProject.sheet;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
class SheetServiceImplTest {

    @Autowired
    SheetService sheetService;

    private Sheet beforeSheet;

    @BeforeEach
    void setup() {

        for (int i = 0; i < 3; i++) {
            //given
            SheetRegDto sheetDto = new SheetRegDto();

            sheetDto.setSheetBooktitle("serviceTitle" + i);
            sheetDto.setSheetBookauthor("serviceBookAuthor");
            sheetDto.setSheetBookpublisher("servicePub");
            sheetDto.setSheetBookisbn("serviceisbn");
            sheetDto.setSheetPrice(1234);
            sheetDto.setSheetAgegroupName(AgeGroupNames.초등저학년);
            sheetDto.setSheetGenreName(GenreNames.한국고전);
            sheetDto.setSheetContent("testContent");

            String str = Integer.toString(i);

            beforeSheet =
                    sheetService.registerSheet(sheetDto, new UploadFile(str, str), new UploadFile(str, str));
            log.info("before sheet={}", beforeSheet);
        }
    }

    @Test
    void getByAgeGroupTest() {
        SheetCri cri = new SheetCri(1,5, SearchTypes.PUBLISHER, "service", SortCries.NEWEST);

        List<Sheet> sheetList = sheetService.getAllSheets(GenreNames.한국고전 , AgeGroupNames.초등저학년, cri);

        for (Sheet sheet : sheetList) {
            log.info("sheet = {}", sheet);
        }

        assertThat(sheetList.size()).isEqualTo(3);
    }

    @Test
    void getByGenreTest() {
        SheetCri cri = new SheetCri(1,5, SearchTypes.PUBLISHER, "service", SortCries.NEWEST);

        List<Sheet> sheetList = sheetService.getAllSheets(GenreNames.한국고전, AgeGroupNames.초등저학년, cri);

        for (Sheet sheet : sheetList) {
            log.info("sheet = {}", sheet);
        }

        assertThat(sheetList.size()).isEqualTo(3);

    }

    @Test
    void registerSheetTest() {
        //given
        SheetRegDto sheetDto = new SheetRegDto();

        sheetDto.setSheetBooktitle("serviceTitle");
        sheetDto.setSheetBookauthor("serviceBookAuthor");
        sheetDto.setSheetBookpublisher("servicePub");
        sheetDto.setSheetBookisbn("serviceisbn");
        sheetDto.setSheetPrice(1234);
        sheetDto.setSheetAgegroupName("유아용");
        sheetDto.setSheetGenreName("현대문학");
        sheetDto.setSheetContent("testContent");

        //when
        log.info("sheetDto = {}", sheetDto);
        Sheet sheet =
                sheetService.registerSheet(sheetDto, new UploadFile("", ""), new UploadFile("", ""));
        log.info("sheet = {}", sheet);

        Sheet foundSheet =
                sheetService.getSheet(sheet.getSheetNo());
        log.info("foundSheet = {}", foundSheet);


        //then
//        assertThat(sheet.getSheetBookimguuid()).isEqualTo(foundSheet.getSheetBookimguuid());
//        assertThat(sheet.getSheetRegdate()).isNotEqualTo(foundSheet.getSheetRegdate());
        //트랜잭션 처리시 시간이 찍히지 않음. 트랜잭션 전파 + JPA작동방식 때문.
        assertThat(sheet).isEqualTo(foundSheet);


    }

    @Test
    void modifySheetTest() {
        //given
        SheetUpdateDto updateDto = new SheetUpdateDto();
        updateDto.setSheetBooktitle("updatedTitle");
        updateDto.setSheetBookauthor("updatedBookAuthor");
        updateDto.setSheetBookpublisher("updatedPub");
        updateDto.setSheetBookisbn("updatedIsbn");
        updateDto.setSheetPrice(1000);
        updateDto.setSheetBookimgname("updatedImgname");
        updateDto.setSheetFilename("updatedFileuuid");
        updateDto.setSheetFilename("updatedFileName");
        updateDto.setSheetAgegroupName("초등저학년");
        updateDto.setSheetGenreName("동화");

        //when

        log.info("last sheet = {}", beforeSheet);
        log.info("update dto = {}", updateDto);
        sheetService.modifySheet(beforeSheet.getSheetNo(), updateDto);
        Sheet foundSheet = sheetService.getSheet(beforeSheet.getSheetNo());
        log.info("fount sheet = {}", foundSheet);

        //then
        assertThat(foundSheet.getSheetBooktitle()).isEqualTo("updatedTitle");
    }

    @Test
    void removeSheetTest() {

        //given
        Long beforeCnt = sheetService.getSheetCnt(GenreNames.한국고전, AgeGroupNames.초등저학년, SearchTypes.TITLE, "");
        log.info("before count = {}", beforeCnt);

        //when
        sheetService.removeSheet(beforeSheet.getSheetNo());

        //then
        assertThat(beforeCnt - 1)
                .isEqualTo(sheetService.getSheetCnt(GenreNames.한국고전, AgeGroupNames.초등저학년, SearchTypes.TITLE, "") - 1);
    }
}
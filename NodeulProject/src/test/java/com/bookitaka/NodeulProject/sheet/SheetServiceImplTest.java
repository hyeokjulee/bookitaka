package com.bookitaka.NodeulProject.sheet;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
class SheetServiceImplTest {

    @Autowired
    SheetService sheetService;

    Sheet beforeSheet;

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
            sheetDto.setSheetBookimgname("serviceimgname");
            sheetDto.setSheetFilename("testFileName");
            sheetDto.setSheetAgegroupName("유아용");
            sheetDto.setSheetGenreName("현대문학");
            sheetDto.setSheetContent("testContent");

            beforeSheet = sheetService.registerSheet(sheetDto);
            log.info("before sheet={}", beforeSheet);
        }
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
        sheetDto.setSheetBookimgname("serviceimgname");
        sheetDto.setSheetFilename("testFileName");
        sheetDto.setSheetAgegroupName("유아용");
        sheetDto.setSheetGenreName("현대문학");
        sheetDto.setSheetContent("testContent");

        //when
        log.info("sheetDto = {}", sheetDto);
        Sheet sheet = sheetService.registerSheet(sheetDto);
        log.info("sheet = {}", sheet);

        Sheet foundSheet = sheetService.getSheet(sheet.getSheetNo());
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
        Long beforeCnt = sheetService.getSheetCnt();
        log.info("before count = {}", beforeCnt);

        //when
        sheetService.removeSheet(beforeSheet.getSheetNo());

        //then
        assertThat(beforeCnt - 1).isEqualTo(sheetService.getSheetCnt());

    }


}
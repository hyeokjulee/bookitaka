package com.bookitaka.NodeulProject.sheet;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
class SheetRepositoryImplTest {

    @Autowired
    SheetRepositoryImpl sheetRepository;

    @Autowired
    EntityManager em;

    SheetAgegroup lastAgeGroup;
    SheetGenre lastGenre;
    Sheet lastSheet;

    @BeforeEach
    void setup() {

        for (int i = 0; i < 3; i++) {
            SheetAgegroup beforeGroup = new SheetAgegroup();
            beforeGroup.setSheetAgegroupName("testAgegroup " + i);
            lastAgeGroup = beforeGroup;
            em.persist(beforeGroup);

            SheetGenre beforeGenre = new SheetGenre();
            beforeGenre.setSheetGenreName("testGenre " + i);
            lastGenre = beforeGenre;
            em.persist(beforeGenre);

            Sheet sheet = new Sheet();
            sheet.setSheetBooktitle("testTitle");
            sheet.setSheetBookauthor("testBookAuthor");
            sheet.setSheetBookpublisher("testPub");
            sheet.setSheetBookisbn("testisbn");
            sheet.setSheetPrice(1234);
            sheet.setSheetBookimguuid("testimguuid" + i);
            sheet.setSheetBookimgname("testimgname");
            sheet.setSheetFilename("testFileuuid" + i);
            sheet.setSheetFilename("testFileName");
            sheet.setSheetAgegroup(beforeGroup);
            sheet.setSheetGenre(beforeGenre);
            sheet.setSheetContent("testContent");

            Sheet createdBeforeSheet = sheetRepository.createSheet(sheet);
            log.info("created before={}",  createdBeforeSheet);
            lastSheet = sheet;
        }

    }

    @Test
    void createSheetTest() {

        //given
        SheetAgegroup agegroup = new SheetAgegroup();
        agegroup.setSheetAgegroupName("testAgegroup");
        em.persist(agegroup);

        SheetGenre genre = new SheetGenre();
        genre.setSheetGenreName("testGenre");
        em.persist(genre);

        Sheet sheet = new Sheet();
        sheet.setSheetBooktitle("testTitle");
        sheet.setSheetBookauthor("testBookAuthor");
        sheet.setSheetBookpublisher("testPub");
        sheet.setSheetBookisbn("testisbn");
        sheet.setSheetPrice(1234);
        sheet.setSheetBookimguuid("testimguuid");
        sheet.setSheetBookimgname("testimgname");
        sheet.setSheetFilename("testFileuuid");
        sheet.setSheetFilename("testFileName");
        sheet.setSheetAgegroup(agegroup);
        sheet.setSheetGenre(genre);

        sheet.setSheetContent("testContent");

        //when
        Sheet createdSheet = sheetRepository.createSheet(sheet);
        log.info("created={}",  createdSheet);
        Sheet findedSheet = sheetRepository.findSheetByNo(createdSheet.getSheetNo()).orElse(null);
        log.info("fined={}", findedSheet);

        //then
        Assertions.assertThat(createdSheet).isEqualTo(findedSheet);


    }

    @Test
    void findSheetByNoTest() {
    }

    @Test
    void updateSheetTest() {

        //given
        SheetUpdateDto sheet = new SheetUpdateDto();
        sheet.setSheetBooktitle("updatedTitle");
        sheet.setSheetBookauthor("updatedBookAuthor");
        sheet.setSheetBookpublisher("updatedPub");
        sheet.setSheetBookisbn("updatedIsbn");
        sheet.setSheetPrice(0000);
        sheet.setSheetBookimguuid("updatedImguuid");
        sheet.setSheetBookimgname("updatedImgname");
        sheet.setSheetFilename("updatedFileuuid");
        sheet.setSheetFilename("updatedFileName");
        sheet.setSheetAgegroupName(lastAgeGroup.getSheetAgegroupName());
        sheet.setSheetGenreName(lastGenre.getSheetGenreName());

        //when

        log.info("last sheet = {}", lastSheet);
        log.info("update Sheet = {}", sheet);
        sheetRepository.updateSheet(lastSheet.getSheetNo(), sheet);
        Sheet fined = sheetRepository.findSheetByNo(lastSheet.getSheetNo()).orElse(null);
        log.info("fined sheet = {}", fined);

        //then
        Assertions.assertThat(fined.getSheetBooktitle()).isEqualTo("updatedTitle");

    }

    @Test
    void deleteSheetTest() {

        //when
        sheetRepository.deleteSheet(lastSheet.getSheetNo());

        //then
        Assertions.assertThat(sheetRepository.countSheet()).isEqualTo(2);

    }
}
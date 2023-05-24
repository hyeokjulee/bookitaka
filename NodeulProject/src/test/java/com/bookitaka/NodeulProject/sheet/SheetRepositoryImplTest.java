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

import static org.assertj.core.api.Assertions.assertThat;
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
            beforeGroup.setSheetAgegroupName("testAgegroup");
            lastAgeGroup = beforeGroup;
            em.persist(beforeGroup);

            SheetGenre beforeGenre = new SheetGenre();
            beforeGenre.setSheetGenreName("testGenre");
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
            sheet.setSheetFileuuid("testFileuuid" + i);
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
        assertThat(createdSheet).isEqualTo(findedSheet);


    }

    @Test
    void findSheetByNoTest() {
        Sheet foundSheet = sheetRepository.findSheetByNo(lastSheet.getSheetNo()).orElse(null);
        assertThat(foundSheet.getSheetNo()).isEqualTo(lastSheet.getSheetNo());
    }

    @Test
    void findAllTest() {
        SheetCri cri = new SheetCri(1, 3, SearchTypes.TITLE, "test");

        List<Sheet> sheetList = sheetRepository.findAllSheet(cri);

        for (Sheet sheet : sheetList) {
            log.info("sheet = {}", sheet);
        }

        assertThat(sheetList.size()).isEqualTo(3);

    }

    @Test
    void findAllByGenreTest() {
        SheetCri cri = new SheetCri(1,5, SearchTypes.PUBLISHER, "test");

        List<Sheet> sheetList = sheetRepository.findAllSheetByGenre("testGenre", cri);

        for (Sheet sheet : sheetList) {
            log.info("sheet = {}", sheet);
        }

        assertThat(sheetList.size()).isEqualTo(3);

    }

    @Test
    void findAllByAgeGroupTest() {
        SheetCri cri = new SheetCri(1,5, SearchTypes.AUTHOR, "test");

        List<Sheet> sheetList = sheetRepository.findAllSheetByGenre("testAgegroup", cri);

        for (Sheet sheet : sheetList) {
            log.info("sheet = {}", sheet);
        }

        assertThat(sheetList.size()).isEqualTo(3);

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
        assertThat(fined.getSheetBooktitle()).isEqualTo("updatedTitle");

    }

    @Test
    void deleteSheetTest() {

        //when
        sheetRepository.deleteSheet(lastSheet.getSheetNo());

        //then
        assertThat(sheetRepository.countSheet()).isEqualTo(2);

    }
}
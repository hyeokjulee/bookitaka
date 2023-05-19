package com.bookitaka.NodeulProject.sheet;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
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
    }

    @Test
    void deleteSheetTest() {
    }
}
package com.bookitaka.NodeulProject.sheet;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Repository
@Transactional
public class SheetRepositoryImpl implements SheetRepository{

    private final EntityManager em;

    public SheetRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Sheet createSheet(Sheet sheet) {
        em.persist(sheet);
        return sheet;
    }

    @Override
    public List<Sheet> findAllSheetByGenre(String genre) {

        return null;
    }

    @Override
    public List<Sheet> findAllSheetByAgeGroup(String ageGroup) {
        return null;
    }

    @Override
    public Optional<Sheet> findSheetByNo(int sheetNo) {
        Sheet sheet = em.find(Sheet.class, sheetNo);
        return Optional.ofNullable(sheet);
    }

    @Override
    public int updateSheet(int sheetNo, SheetUpdateDto sheetUpdateDto) {
        Sheet findSheet = em.find(Sheet.class, sheetNo);
        if (findSheet !=null) {
            findSheet.setSheetBooktitle(sheetUpdateDto.getSheetBooktitle());
            findSheet.setSheetBookauthor(sheetUpdateDto.getSheetBookauthor());
            findSheet.setSheetPublisher(sheetUpdateDto.getSheetPublisher());
            findSheet.setSheetBookisbn(sheetUpdateDto.getSheetBookisbn());
            findSheet.setPrice(sheetUpdateDto.getPrice());
            findSheet.setSheetBookimguuid(sheetUpdateDto.getSheetBookimguuid());
            findSheet.setSheetBookimgename(sheetUpdateDto.getSheetBookimgename());
            findSheet.setSheetFilename(sheetUpdateDto.getSheetFilename());
            findSheet.setSheetFilename(sheetUpdateDto.getSheetFilename());
            findSheet.setSheetGenreNo(sheetUpdateDto.getSheetGenreNo());
            findSheet.setSheetAgegroupNo(sheetUpdateDto.getSheetAgegroupNo());
            findSheet.setSheetContent(sheetUpdateDto.getSheetContent());
            return 1;
        }
        return 0;
    }

    @Override
    public int deleteSheet(int sheetNo) {
        Sheet findSheet = em.find(Sheet.class, sheetNo);
        if (findSheet !=null) {
            em.remove(findSheet);
            return 1;
        }
        return 0;
    }
}

package com.bookitaka.NodeulProject.sheet;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
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
    public List<Sheet> findAllSheet() {
        return null;
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
    public boolean updateSheet(int sheetNo, SheetUpdateDto sheetUpdateDto) {
        Sheet findSheet = em.find(Sheet.class, sheetNo);

        if (findSheet !=null) {
            findSheet.setSheetBooktitle(sheetUpdateDto.getSheetBooktitle());
            findSheet.setSheetBookauthor(sheetUpdateDto.getSheetBookauthor());
            findSheet.setSheetBookpublisher(sheetUpdateDto.getSheetBookpublisher());
            findSheet.setSheetBookisbn(sheetUpdateDto.getSheetBookisbn());
            findSheet.setSheetPrice(sheetUpdateDto.getSheetPrice());
            findSheet.setSheetBookimguuid(sheetUpdateDto.getSheetBookimguuid());
            findSheet.setSheetBookimgname(sheetUpdateDto.getSheetBookimgname());
            findSheet.setSheetFilename(sheetUpdateDto.getSheetFilename());
            findSheet.setSheetFilename(sheetUpdateDto.getSheetFilename());
            findSheet.setSheetGenre(new SheetGenre(sheetUpdateDto.getSheetGenreName()));
            findSheet.setSheetAgegroup(new SheetAgegroup(sheetUpdateDto.getSheetAgegroupName()));
            findSheet.setSheetContent(sheetUpdateDto.getSheetContent());
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteSheet(int sheetNo) {
        Sheet findSheet = em.find(Sheet.class, sheetNo);
        if (findSheet !=null) {
            em.remove(findSheet);
            return true;
        }
        return false;
    }

    @Override
    public Long countSheet() {
        Query query = em.createQuery("SELECT COUNT(*) FROM Sheet");
        return (Long) query.getSingleResult();
        //카운트 하는법 고민중...
    }
}

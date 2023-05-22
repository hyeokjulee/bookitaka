package com.bookitaka.NodeulProject.sheet;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Repository
@Transactional
@RequiredArgsConstructor
public class SheetRepositoryImpl implements SheetRepository{

    private final EntityManager em;
    private final JPAQueryFactory qf;

//    JPAQuery query = new JPAQuery(em);
//
    QSheet qSheet = new QSheet("m");


    @Override
    public Sheet createSheet(Sheet sheet) {
        em.persist(sheet);
        return sheet;
    }

    @Override
    public List<Sheet> findAllSheet(SheetCri cri) {
        List<Sheet> sheetList = null;
        if (cri.getSearchType().equals("제목")) {
            sheetList = (List<Sheet>) qf.selectFrom(qSheet)
                    .where(qSheet.sheetBooktitle.like("%" + cri.getSearchWord() + "%"))
                    .offset((cri.getPageNum()-1) * cri.getAmount() + 1).limit(cri.getAmount());
        } else if (cri.getSearchType().equals("작가")) {
            sheetList = (List<Sheet>) qf.selectFrom(qSheet)
                    .where(qSheet.sheetBookauthor.like("%" + cri.getSearchWord() + "%"))
                    .offset((cri.getPageNum()-1) * cri.getAmount() + 1).limit(cri.getAmount());
        } else if (cri.getSearchType().equals("출판사")) {
            sheetList = (List<Sheet>) qf.selectFrom(qSheet)
                    .where(qSheet.sheetBookpublisher.like("%" + cri.getSearchWord() + "%"))
                    .offset((cri.getPageNum()-1) * cri.getAmount() + 1).limit(cri.getAmount());
        }
        return sheetList;
    }

    @Override
    public List<Sheet> findAllSheetByGenre(String genre) {
        List<Sheet> sheetList = (List<Sheet>) qf.selectFrom(qSheet);


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
            findSheet.setSheetBookimgname(sheetUpdateDto.getSheetBookimgname());
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

    @Override
    public SheetGenre findSheetGenreByName(String genreName) {
        QSheetGenre sheetGenre = QSheetGenre.sheetGenre;

        return qf.selectFrom(sheetGenre)
                .where(sheetGenre.SheetGenreName.eq(genreName))
                .fetchOne();
    }

    @Override
    public SheetAgegroup findSheetAgeGroupByName(String ageGroupName) {
        QSheetAgegroup sheetAgegroup = QSheetAgegroup.sheetAgegroup;

        return qf.selectFrom(sheetAgegroup)
                .where(sheetAgegroup.SheetAgegroupName.eq(ageGroupName))
                .fetchOne();
    }

    @Override
    public List<String> findAllAgeGroup() {
        QSheetAgegroup sheetAgegroup = QSheetAgegroup.sheetAgegroup;


        return null;
    }

    @Override
    public List<String> findAllGenre() {
        return null;
    }


}

package com.bookitaka.NodeulProject.sheet;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
@Transactional
@RequiredArgsConstructor
public class SheetRepositoryImpl implements SheetRepository{

    private final EntityManager em;
    private final JPAQueryFactory qf;

    QSheet qSheet = new QSheet("m");


    @Override
    public Sheet createSheet(Sheet sheet) {
        em.persist(sheet);
        return sheet;
    }

    @Override
    public List<Sheet> findAllSheet(SheetCri cri) {
        List<Sheet> sheetList = null;

        if (cri.getSearchType().equals(SearchTypes.TITLE)) {
            return qf.selectFrom(qSheet)
                    .where(qSheet.sheetBooktitle.like("%" + cri.getSearchWord() + "%"))
                    .offset(cri.getPageNum() - 1).limit(cri.getAmount()).fetch();
        } else if (cri.getSearchType().equals(SearchTypes.AUTHOR)) {
            return qf.selectFrom(qSheet)
                    .where(qSheet.sheetBookauthor.like("%" + cri.getSearchWord() + "%"))
                    .offset(cri.getPageNum() - 1).limit(cri.getAmount()).fetch();
        } else if (cri.getSearchType().equals(SearchTypes.PUBLISHER)) {
            return qf.selectFrom(qSheet)
                    .where(qSheet.sheetBookpublisher.like("%" + cri.getSearchWord() + "%"))
                    .offset(cri.getPageNum()-1).limit(cri.getAmount()).fetch();
        }
        return null;
    }

    @Override
    public List<Sheet> findAllSheetByGenre(String genre, SheetCri cri) {

        if (cri.getSearchType().equals(SearchTypes.TITLE)) {
            return qf.selectFrom(qSheet)
                    .where(qSheet.sheetGenre.sheetGenreName.eq(genre))
                    .where(qSheet.sheetBooktitle.like("%" + cri.getSearchWord() + "%"))
                    .offset(cri.getPageNum() - 1).limit(cri.getAmount()).fetch();
        } else if (cri.getSearchType().equals(SearchTypes.AUTHOR)) {
            return qf.selectFrom(qSheet)
                    .where(qSheet.sheetGenre.sheetGenreName.eq(genre))
                    .where(qSheet.sheetBookauthor.like("%" + cri.getSearchWord() + "%"))
                    .offset(cri.getPageNum() - 1).limit(cri.getAmount()).fetch();
        } else if (cri.getSearchType().equals(SearchTypes.PUBLISHER)) {
            return qf.selectFrom(qSheet)
                    .where(qSheet.sheetGenre.sheetGenreName.eq(genre))
                    .where(qSheet.sheetBookpublisher.like("%" + cri.getSearchWord() + "%"))
                    .offset(cri.getPageNum()-1).limit(cri.getAmount()).fetch();
        }


        return null;
    }

    @Override
    public List<Sheet> findAllSheetByAgeGroup(String ageGroup, SheetCri cri) {

        if (cri.getSearchType().equals(SearchTypes.TITLE)) {
            return qf.selectFrom(qSheet)
                    .where(qSheet.sheetAgegroup.sheetAgegroupName.eq(ageGroup))
                    .where(qSheet.sheetBooktitle.like("%" + cri.getSearchWord() + "%"))
                    .offset(cri.getPageNum() - 1).limit(cri.getAmount()).fetch();
        } else if (cri.getSearchType().equals(SearchTypes.AUTHOR)) {
            return qf.selectFrom(qSheet)
                    .where(qSheet.sheetAgegroup.sheetAgegroupName.eq(ageGroup))
                    .where(qSheet.sheetBookauthor.like("%" + cri.getSearchWord() + "%"))
                    .offset(cri.getPageNum() - 1).limit(cri.getAmount()).fetch();
        } else if (cri.getSearchType().equals(SearchTypes.PUBLISHER)) {
            return qf.selectFrom(qSheet)
                    .where(qSheet.sheetAgegroup.sheetAgegroupName.eq(ageGroup))
                    .where(qSheet.sheetBookpublisher.like("%" + cri.getSearchWord() + "%"))
                    .offset(cri.getPageNum()-1).limit(cri.getAmount()).fetch();
        }

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
        QSheet sheet = QSheet.sheet;
        return qf.select(sheet.sheetNo.count()).from(sheet).fetchOne();
    }


}

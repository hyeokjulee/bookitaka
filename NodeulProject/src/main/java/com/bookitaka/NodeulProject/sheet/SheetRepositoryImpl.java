package com.bookitaka.NodeulProject.sheet;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Slf4j
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
        log.info("cri = {}", cri);

        JPAQuery<Sheet> query = qf.selectFrom(qSheet);

        if (cri.getSearchType().equals(SearchTypes.TITLE)) {
            query.where(qSheet.sheetBooktitle.like("%" + cri.getSearchWord() + "%"));
        } else if (cri.getSearchType().equals(SearchTypes.AUTHOR)) {
            query.where(qSheet.sheetBookauthor.like("%" + cri.getSearchWord() + "%"));
        } else if (cri.getSearchType().equals(SearchTypes.PUBLISHER)) {
            query.where(qSheet.sheetBookpublisher.like("%" + cri.getSearchWord() + "%"));
        }

        query.offset((cri.getPageNum() - 1) * cri.getAmount()).limit(cri.getAmount());

        if (cri.getSort().equals(SortCries.NEWEST)) {
            query.orderBy(qSheet.sheetNo.desc());
        } else if (cri.getSort().equals(SortCries.HIT)) {
            query.orderBy(qSheet.sheetHit.desc());
        } else if (cri.getSort().equals(SortCries.BUYCNT)) {
            query.orderBy(qSheet.sheetBuycnt.desc());
        }

        return query.fetch();
    }



    @Override
    public List<Sheet> findAllSheetByGenre(String genre, SheetCri cri) {

        JPAQuery<Sheet> query = qf.selectFrom(qSheet).where(qSheet.sheetGenre.sheetGenreName.eq(genre));

        if (cri.getSearchType().equals(SearchTypes.TITLE)) {
            query.where(qSheet.sheetBooktitle.like("%" + cri.getSearchWord() + "%"));
        } else if (cri.getSearchType().equals(SearchTypes.AUTHOR)) {
            query.where(qSheet.sheetBookauthor.like("%" + cri.getSearchWord() + "%"));
        } else if (cri.getSearchType().equals(SearchTypes.PUBLISHER)) {
            query.where(qSheet.sheetBookpublisher.like("%" + cri.getSearchWord() + "%"));
        }

        query.offset((cri.getPageNum() - 1) * cri.getAmount()).limit(cri.getAmount());

        if (cri.getSort().equals(SortCries.NEWEST)) {
            query.orderBy(qSheet.sheetNo.desc());
        } else if (cri.getSort().equals(SortCries.HIT)) {
            query.orderBy(qSheet.sheetHit.desc());
        } else if (cri.getSort().equals(SortCries.BUYCNT)) {
            query.orderBy(qSheet.sheetBuycnt.desc());
        }

        return query.fetch();

    }

    @Override
    public List<Sheet> findAllSheetByAgeGroup(String ageGroup, SheetCri cri) {

        JPAQuery<Sheet> query = qf.selectFrom(qSheet).where(qSheet.sheetAgegroup.sheetAgegroupName.eq(ageGroup));

        if (cri.getSearchType().equals(SearchTypes.TITLE)) {
            query.where(qSheet.sheetBooktitle.like("%" + cri.getSearchWord() + "%"));
        } else if (cri.getSearchType().equals(SearchTypes.AUTHOR)) {
            query.where(qSheet.sheetBookauthor.like("%" + cri.getSearchWord() + "%"));
        } else if (cri.getSearchType().equals(SearchTypes.PUBLISHER)) {
            query.where(qSheet.sheetBookpublisher.like("%" + cri.getSearchWord() + "%"));
        }

        query.offset((cri.getPageNum() - 1) * cri.getAmount()).limit(cri.getAmount());

        if (cri.getSort().equals(SortCries.NEWEST)) {
            query.orderBy(qSheet.sheetNo.desc());
        } else if (cri.getSort().equals(SortCries.HIT)) {
            query.orderBy(qSheet.sheetHit.desc());
        } else if (cri.getSort().equals(SortCries.BUYCNT)) {
            query.orderBy(qSheet.sheetBuycnt.desc());
        }

        return query.fetch();
    }

    @Override
    public Optional<Sheet> findSheetByNo(int sheetNo) {
        Sheet sheet = em.find(Sheet.class, sheetNo);

        return Optional.ofNullable(sheet);
    }

    @Override
    public boolean plusOneSheetHit(int sheetNo) {
        Sheet sheet = em.find(Sheet.class, sheetNo);

        //조회수 하나 올리기
        if (sheet != null) {
            sheet.setSheetHit(sheet.getSheetHit() + 1);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateSheet(int sheetNo, SheetUpdateDto sheetUpdateDto) {
        Sheet findSheet = em.find(Sheet.class, sheetNo);

        if (findSheet !=null) {
            ModelMapper modelMapper = new ModelMapper();

            modelMapper.map(sheetUpdateDto, findSheet);
            em.merge(findSheet);
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
    public Long countSheet(String searchType, String searchWord) {
        if (searchType.equals(SearchTypes.TITLE)) {
            return qf.select(qSheet.sheetNo.count()).from(qSheet)
                    .where(qSheet.sheetBooktitle.like("%" + searchWord + "%")).fetchOne();
        } else if (searchType.equals(SearchTypes.AUTHOR)) {
            return qf.select(qSheet.sheetNo.count()).from(qSheet)
                    .where(qSheet.sheetBookauthor.like("%" + searchWord + "%")).fetchOne();
        } else if (searchType.equals(SearchTypes.PUBLISHER)) {
            return qf.select(qSheet.sheetNo.count()).from(qSheet)
                    .where(qSheet.sheetBookpublisher.like("%" + searchWord + "%")).fetchOne();
        }

        return 0L;
    }

    @Override
    public Long countSheetByGenre(String genre, String searchType, String searchWord) {
        if (searchType.equals(SearchTypes.TITLE)) {
            return qf.select(qSheet.sheetNo.count()).from(qSheet)
                    .where(qSheet.sheetGenre.sheetGenreName.eq(genre))
                    .where(qSheet.sheetBooktitle.like("%" + searchWord + "%")).fetchOne();
        } else if (searchType.equals(SearchTypes.AUTHOR)) {
            return qf.select(qSheet.sheetNo.count()).from(qSheet)
                    .where(qSheet.sheetGenre.sheetGenreName.eq(genre))
                    .where(qSheet.sheetBookauthor.like("%" + searchWord + "%")).fetchOne();
        } else if (searchType.equals(SearchTypes.PUBLISHER)) {
            return qf.select(qSheet.sheetNo.count()).from(qSheet)
                    .where(qSheet.sheetGenre.sheetGenreName.eq(genre))
                    .where(qSheet.sheetBookpublisher.like("%" + searchWord + "%")).fetchOne();
        }

        return 0L;
    }

    @Override
    public Long countSheetByAgeGroup(String ageGroup, String searchType, String searchWord) {
        if (searchType.equals(SearchTypes.TITLE)) {
            return qf.select(qSheet.sheetNo.count()).from(qSheet)
                    .where(qSheet.sheetAgegroup.sheetAgegroupName.eq(ageGroup))
                    .where(qSheet.sheetBooktitle.like("%" + searchWord + "%")).fetchOne();
        } else if (searchType.equals(SearchTypes.AUTHOR)) {
            return qf.select(qSheet.sheetNo.count()).from(qSheet)
                    .where(qSheet.sheetAgegroup.sheetAgegroupName.eq(ageGroup))
                    .where(qSheet.sheetBookauthor.like("%" + searchWord + "%")).fetchOne();
        } else if (searchType.equals(SearchTypes.PUBLISHER)) {
            return qf.select(qSheet.sheetNo.count()).from(qSheet)
                    .where(qSheet.sheetAgegroup.sheetAgegroupName.eq(ageGroup))
                    .where(qSheet.sheetBookpublisher.like("%" + searchWord + "%")).fetchOne();
        }

        return 0L;
    }

    @Override
    public String findFileNameByUuid(String uuid) {
        return qf.selectFrom(qSheet).where(qSheet.sheetFileuuid.eq(uuid)).fetchOne().getSheetFilename();
    }


}

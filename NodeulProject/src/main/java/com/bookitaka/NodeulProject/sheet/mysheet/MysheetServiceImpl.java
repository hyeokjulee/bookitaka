package com.bookitaka.NodeulProject.sheet.mysheet;

import com.bookitaka.NodeulProject.member.model.Member;
import com.bookitaka.NodeulProject.sheet.SearchTypes;
import com.bookitaka.NodeulProject.sheet.Sheet;
import com.bookitaka.NodeulProject.sheet.SheetRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MysheetServiceImpl implements MysheetService{

    private final MysheetRepository mysheetRepository;
    private final SheetRepository sheetRepository;


    @Override
    public List<SheetForMemberDto> getAllMysheetByMember(MysheetCri mysheetCri, Member member) {
        //쿼리 DSL로 검색조건에 따른 검색 구현
        QMysheet QMysheet = com.bookitaka.NodeulProject.sheet.mysheet.QMysheet.mysheet;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(QMysheet.member.eq(member));

        if (mysheetCri.getSearchType().equals(SearchTypes.TITLE)) {
            builder.and(QMysheet.sheet.sheetBooktitle.contains(mysheetCri.getSearchWord()));
        } else if (mysheetCri.getSearchType().equals(SearchTypes.AUTHOR)) {
            builder.and(QMysheet.sheet.sheetBooktitle.contains(mysheetCri.getSearchWord()));
        } else if (mysheetCri.getSearchType().equals(SearchTypes.PUBLISHER)) {
            builder.and(QMysheet.sheet.sheetBooktitle.contains(mysheetCri.getSearchWord()));
        }


        Predicate predicate = builder.getValue();

        OrderSpecifier<Date> orderSpecifier = QMysheet.mysheetStartdate.desc();

        //페이징 구현
        Pageable pageable = PageRequest.of(mysheetCri.getPageNum()-1, mysheetCri.getAmount(), Sort.by(Sort.Direction.DESC, "mysheetStartdate"));

        //모든 mySheet가져옴;
        List<Mysheet> mysheetList = mysheetRepository.findAll(predicate, pageable).getContent();

        List<SheetForMemberDto> sheetForMemberDtoList = new ArrayList<>();

        //모든 mysheet에 해당하는 sheet가져와서 dtoList에 넣기
        for (Mysheet mysheet: mysheetList) {
            SheetForMemberDto sheetForMemberDto = new SheetForMemberDto();
            Sheet sheet = mysheet.getSheet();

            sheetForMemberDto.setSheetNo(sheet.getSheetNo());
            sheetForMemberDto.setSheetBooktitle(sheet.getSheetBooktitle());
            sheetForMemberDto.setSheetBookauthor(sheet.getSheetBookauthor());
            sheetForMemberDto.setSheetBookpublisher(sheet.getSheetBookpublisher());
            sheetForMemberDto.setMysheetEndDate(mysheet.getMysheetEnddate());
            sheetForMemberDto.setSheetBookImgFullName(sheet.getSheetBookimguuid() + sheet.getSheetBookimgname());

            if (checkMySheetIsAvailable(mysheet)) {
                sheetForMemberDto.setTimeLimit(true);
                sheetForMemberDto.setSheetFileUuid(sheet.getSheetFileuuid());
            } else {
                sheetForMemberDto.setTimeLimit(false);
            }
            sheetForMemberDtoList.add(sheetForMemberDto);
        }

        return sheetForMemberDtoList;
    }

    @Override
    public Long getMySheetCnt(String searchType, String searchWord, Member member) {
        QMysheet QMysheet = com.bookitaka.NodeulProject.sheet.mysheet.QMysheet.mysheet;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(QMysheet.member.eq(member));

        Predicate predicate = builder.getValue();

        return mysheetRepository.count(predicate);
    }

    @Override
    public Mysheet canIDownloadSheet(String fileUuid, Member member) {
        return mysheetRepository.findFirstBySheet_SheetFileuuidAndMemberOrderByMysheetEnddateDesc(fileUuid, member);
    }

    @Override
    public boolean checkMySheetIsAvailable(Mysheet mysheet) {

        Date mysheetEnddate = mysheet.getMysheetEnddate();
        Date currentDate = new Date();

        //기한 지나면
        if (mysheetEnddate.before(currentDate)) {
            //기한 지나면 false
            return false;
        } else {
            //기한 안지나면 true
            return true;
        }
    }

    @Override
    public Page<Mysheet> getAllMysheetForAdmin() {
        return null;
    }
}

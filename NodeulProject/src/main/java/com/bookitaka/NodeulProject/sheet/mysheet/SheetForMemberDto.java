package com.bookitaka.NodeulProject.sheet.mysheet;

import lombok.Data;

import java.util.Date;

@Data
public class SheetForMemberDto {

    //내 독후활동지 보기에서 띄울 데이터들 목록입니다.
    private int sheetNo;
    private String sheetBooktitle;
    private String sheetBookauthor;
    private String sheetBookpublisher;

    private String sheetBookImgFullName;

    //다운로드 만료일
    private Date mysheetEndDate;

    //독후활동지 다운로드 링크
    private String sheetFileUuid;

    //기간 지났는지 여부 지나면 0, 다운 가능하면 1
    private boolean timeLimit;

}

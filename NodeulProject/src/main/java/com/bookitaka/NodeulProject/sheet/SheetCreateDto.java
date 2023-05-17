package com.bookitaka.NodeulProject.sheet;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

public class SheetCreateDto {

    private int sheetNo;
    private String sheetBooktitle;
    private String sheetBookauthor;
    private String sheetPublisher;
    private String sheetBookisbn;
    private Integer price;
    private String sheetBookimguuid;
    private String sheetBookimgename;
    private String sheetFileuuid;
    private String sheetFilename;
    private Integer sheetAgegroupNo;
    private Integer sheetGenreNo;
    private String sheetContent;

}

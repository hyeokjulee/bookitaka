package com.bookitaka.NodeulProject.sheet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class SheetUpdateDto {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private Date sheetModdate;


}

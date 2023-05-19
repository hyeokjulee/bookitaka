package com.bookitaka.NodeulProject.sheet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SheetUpdateDto {

    private String sheetBooktitle;
    private String sheetBookauthor;
    private String sheetBookpublisher;
    private String sheetBookisbn;
    private Integer sheetPrice;
    private String sheetBookimguuid;
    private String sheetBookimgname;
    private String sheetFileuuid;
    private String sheetFilename;
    private String sheetAgegroupName;
    private String sheetGenreName;
    private String sheetContent;


}

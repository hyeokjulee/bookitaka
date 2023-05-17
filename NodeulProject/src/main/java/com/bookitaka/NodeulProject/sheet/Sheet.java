package com.bookitaka.NodeulProject.sheet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Sheet {

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
    private int sheetBuycnt;
    private int sheetHit;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Date sheetReqdate;

    @LastModifiedDate
    private Date sheetModdate;


}

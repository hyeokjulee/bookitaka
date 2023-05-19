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
public class SheetCreateDto {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int sheetNo;
    private String sheetBooktitle;
    private String sheetBookauthor;
    private String sheetPublisher;
    private String sheetBookisbn;
    private Integer sheetPrice;
    private String sheetBookimguuid;
    private String sheetBookimgename;
    private String sheetFileuuid;
    private String sheetFilename;

    private String sheetAgegroupName;

    private String sheetGenre;

    private String sheetContent;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Date sheetRegdate;

}

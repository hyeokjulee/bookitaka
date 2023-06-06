package com.bookitaka.NodeulProject.sheet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Controller;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@EntityListeners(AuditingEntityListener.class)
public class Sheet {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int sheetNo;
    private String sheetBooktitle;
    private String sheetBookauthor;
    private String sheetBookpublisher;
    private String sheetBookisbn;
    private Integer sheetPrice;
    private String sheetBookimguuid;
    private String sheetBookimgname;
    private String sheetFileuuid;
    private String sheetFilename;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn (name = "sheetAgegroupNo")
    private SheetAgegroup sheetAgegroup;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn (name = "sheetGenreNo")
    private SheetGenre sheetGenre;

    private String sheetContent;
    private int sheetBuycnt;
    private int sheetHit;

    private Date sheetRegdate;

    @LastModifiedDate
    private Date sheetModdate;


}

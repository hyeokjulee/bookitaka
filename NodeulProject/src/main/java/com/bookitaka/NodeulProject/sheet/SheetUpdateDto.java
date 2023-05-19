package com.bookitaka.NodeulProject.sheet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn (name = "sheetAgegroupNo")
    private SheetAgegroup sheetAgegroup;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn (name = "sheetGrenreNo")
    private SheetGenre sheetGenre;
    private String sheetContent;
    private Date sheetModdate;


}

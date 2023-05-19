package com.bookitaka.NodeulProject.sheet;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class SheetAgegroup {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int SheetAgegroupNo;

    private String SheetAgegroupName;

    public SheetAgegroup() {
    }

    public SheetAgegroup(String sheetAgegroupName) {
        SheetAgegroupName = sheetAgegroupName;
    }
}

package com.bookitaka.NodeulProject.sheet;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class SheetGenre {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int SheetGenreNo;

    private String SheetGenreName;

    public SheetGenre() {
    }

    public SheetGenre(String sheetGenreName) {
        SheetGenreName = sheetGenreName;
    }

}

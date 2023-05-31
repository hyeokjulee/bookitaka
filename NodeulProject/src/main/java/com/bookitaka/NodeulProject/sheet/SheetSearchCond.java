package com.bookitaka.NodeulProject.sheet;

import lombok.Data;

@Data
public class SheetSearchCond {

    private String bookTitle;
    private String bookAuthor;
    private String bookPublisher;

    private String division;

    public SheetSearchCond() {
    }

    public SheetSearchCond(String bookTitle, String bookAuthor, String bookPublisher, String division) {
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.bookPublisher = bookPublisher;
        this.division = division;
    }


}

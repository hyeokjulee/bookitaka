package com.bookitaka.NodeulProject.sheet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

    private String sheetBooktitle;
    private String sheetBookauthor;
    private String sheetBookpublisher;
    private String sheetBookisbn;

//    private String sheetBookimguuid;
//    private String sheetBookimgname;
}

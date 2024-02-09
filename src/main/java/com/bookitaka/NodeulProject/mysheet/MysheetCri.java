package com.bookitaka.NodeulProject.mysheet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MysheetCri {

    private int pageNum ;
    private int amount;

    private String searchType;
    private String searchWord;

}

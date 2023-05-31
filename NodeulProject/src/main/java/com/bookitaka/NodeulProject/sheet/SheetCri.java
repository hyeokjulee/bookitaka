package com.bookitaka.NodeulProject.sheet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SheetCri {

    private int pageNum ;
    private int amount;

    private String searchType;
    private String searchWord;

    
}

package com.bookitaka.NodeulProject.sheet;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SheetService {

    public Sheet registerSheet(SheetRegDto sheetRegDto);

    public Sheet getSheet(int sheetNo);

    public List<Sheet> getAllSheetByGenre (SheetSearchCond searchCond);

    public List<Sheet> getAllSheetByAgeGroup(SheetSearchCond searchCond);

    public boolean modifySheet(int sheetNo, SheetUpdateDto sheetUpdateDto);

    public boolean removeSheet(int sheetNo);


}

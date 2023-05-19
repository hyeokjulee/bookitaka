package com.bookitaka.NodeulProject.sheet;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SheetServiceImpl implements SheetService{

    private final SheetRepository sheetRepository;

    @Override
    public Sheet registerSheet(Sheet sheet) {
        return null;
    }

    @Override
    public Sheet getSheet(int sheetNo) {
        return null;
    }

    @Override
    public List<Sheet> getAllSheetByGenre(SheetSearchCond searchCond) {
        return null;
    }

    @Override
    public List<Sheet> getAllSheetByAgeGroup(SheetSearchCond searchCond) {
        return null;
    }

    @Override
    public boolean modifySheet(int sheetNo, SheetUpdateDto sheetUpdateDto) {
        return false;
    }

    @Override
    public boolean removeSheet(int sheetNo) {
        return false;
    }
}

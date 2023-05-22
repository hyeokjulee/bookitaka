package com.bookitaka.NodeulProject.sheet;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SheetServiceImpl implements SheetService{

    private final SheetRepository sheetRepository;

    @Override
    public Sheet registerSheet(SheetRegDto sheetRegDto) {
        Sheet sheet = new Sheet();
        sheet.setSheetBooktitle(sheetRegDto.getSheetBooktitle());
        sheet.setSheetBookauthor(sheetRegDto.getSheetBookauthor());
        sheet.setSheetBookpublisher(sheetRegDto.getSheetBookpublisher());
        sheet.setSheetPrice(sheetRegDto.getSheetPrice());

        sheet.setSheetBookimguuid(UUID.randomUUID().toString());
        sheet.setSheetBookimgname(sheetRegDto.getSheetBookimgename());

        sheet.setSheetFileuuid(UUID.randomUUID().toString());
        sheet.setSheetFilename(sheetRegDto.getSheetFilename());

        sheet.setSheetGenre(sheetRepository.findSheetGenreByName(sheetRegDto.getSheetGenreName()));
        sheet.setSheetAgegroup(sheetRepository.findSheetAgeGroupByName(sheetRegDto.getSheetAgegroupName()));
        sheet.setSheetContent(sheetRegDto.getSheetContent());

        return sheetRepository.createSheet(sheet);
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
        return sheetRepository.updateSheet(sheetNo, sheetUpdateDto);
    }

    @Override
    public boolean removeSheet(int sheetNo) {
        return sheetRepository.deleteSheet(sheetNo);
    }
}

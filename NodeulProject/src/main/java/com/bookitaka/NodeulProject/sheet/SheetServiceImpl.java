package com.bookitaka.NodeulProject.sheet;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SheetServiceImpl implements SheetService{

    private final SheetRepository sheetRepository;
    private final GenreRepository genreRepository;
    private final AgeGroupRepository ageGroupRepository;

    @Override
    public Sheet registerSheet(SheetRegDto sheetRegDto) {
        Sheet sheet = new Sheet();
        sheet.setSheetBooktitle(sheetRegDto.getSheetBooktitle());
        sheet.setSheetBookauthor(sheetRegDto.getSheetBookauthor());
        sheet.setSheetBookpublisher(sheetRegDto.getSheetBookpublisher());
        sheet.setSheetBookisbn(sheetRegDto.getSheetBookisbn());
        sheet.setSheetPrice(sheetRegDto.getSheetPrice());

        sheet.setSheetBookimguuid(UUID.randomUUID().toString());
        sheet.setSheetBookimgname(sheetRegDto.getSheetBookimgname());

        sheet.setSheetFileuuid(UUID.randomUUID().toString());
        sheet.setSheetFilename(sheetRegDto.getSheetFilename());

        sheet.setSheetGenre(genreRepository.findBySheetGenreName(sheetRegDto.getSheetGenreName()));
        sheet.setSheetAgegroup(ageGroupRepository.findBySheetAgegroupName(sheetRegDto.getSheetAgegroupName()));
        sheet.setSheetContent(sheetRegDto.getSheetContent());

        return sheetRepository.createSheet(sheet);
    }

    @Override
    public Sheet getSheet(int sheetNo) {
        return sheetRepository.findSheetByNo(sheetNo).orElse(null);
    }

    @Override
    public Long getSheetCnt() {
        return sheetRepository.countSheet();
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

    @Override
    public List<SheetGenre> getAllSheetGenre() {
        return genreRepository.findAll();
    }

    @Override
    public List<SheetAgegroup> getAllSheetAgeGroup() {
        return ageGroupRepository.findAll();
    }
}

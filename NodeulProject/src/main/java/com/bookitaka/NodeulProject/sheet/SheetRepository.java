package com.bookitaka.NodeulProject.sheet;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface SheetRepository {

    public Sheet createSheet(Sheet sheet);


    public List<Sheet> findAllSheet(SheetCri cri);

    public List<Sheet> findAllSheetByGenre(String genre);

    public List<Sheet> findAllSheetByAgeGroup(String ageGroup);

    public Optional<Sheet> findSheetByNo(int sheetNo);

    public boolean updateSheet(int sheetNo ,SheetUpdateDto sheetUpdateDto);

    public boolean deleteSheet(int sheetNo);

    public Long countSheet();

    public SheetGenre findSheetGenreByName(String genreName);

    public SheetAgegroup findSheetAgeGroupByName(String ageGroupName);

    public List<String> findAllAgeGroup();

    public List<String> findAllGenre();

}

package com.bookitaka.NodeulProject.sheet;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public interface SheetService {

    Sheet registerSheet(SheetRegDto sheetRegDto, UploadFile uploadBookImg, UploadFile uploadSheetFile);

    public UploadFile storeBookImg(MultipartFile sheetBookImg) throws IOException;

    public UploadFile storeSheetFile(MultipartFile sheetFile) throws IOException;


    public Sheet getSheet(int sheetNo);

    public Long getSheetCnt();

    public List<Sheet> getAllSheetByGenre(String genreName, SheetCri cri);

    public List<Sheet> getAllSheetByAgeGroup(String ageGroupName, SheetCri cri);

    public boolean modifySheet(int sheetNo, SheetUpdateDto sheetUpdateDto);

    public boolean removeSheet(int sheetNo);

    public List<SheetGenre> getAllSheetGenre();

    public List<SheetAgegroup> getAllSheetAgeGroup();


}

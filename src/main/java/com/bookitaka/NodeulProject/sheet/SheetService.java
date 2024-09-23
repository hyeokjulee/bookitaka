package com.bookitaka.NodeulProject.sheet;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public interface SheetService {

    Sheet registerSheet(SheetRegDto sheetRegDto, UploadFile uploadBookImg, UploadFile uploadSheetFile);

    public UploadFile storeBookImg(MultipartFile sheetBookImg) throws IOException;

    public UploadFile storeSheetFile(MultipartFile sheetFile) throws IOException;


    public Sheet getSheet(int sheetNo);

    public Long getSheetCnt(String genre, String ageGroup, String searchType, String searchWord);

    @Cacheable(cacheNames = "getAllSheets", key = "'sheets:genre:' + #genre + ':ageGroup:' + #ageGroup + ':pageNum:' + #cri.pageNum + ':amount:' + #cri.amount + ':searchType:' + #cri.searchType + ':searchWord:' + #cri.searchWord + ':sort:' + #cri.sort", cacheManager = "sheetCacheManager")
    public List<Sheet> getAllSheets(String genre, String ageGroup, SheetCri cri);

    public boolean modifySheet(int sheetNo, SheetUpdateDto sheetUpdateDto);

    public boolean removeSheet(int sheetNo);

    public boolean removeStoredFile(String filePath);

    public List<SheetGenre> getAllSheetGenre();

    public List<SheetAgegroup> getAllSheetAgeGroup();

    public String getFileNameByUuid(String uuid);

//    public Map<String, Object> searchBook(String keyword, String authorSearch, Integer pageNum);

}

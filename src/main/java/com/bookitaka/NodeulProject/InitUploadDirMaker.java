package com.bookitaka.NodeulProject;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class InitUploadDirMaker {
    @Value("${file.bookImg.dir}")
    private String bookImgDir;

    @Value("${file.sheetFile.dir}")
    private String sheetFileDir;

    @Value("${file.preview.dir}")
    private String previewDir;

    @PostConstruct
    public void init() {
        List<File> folders = new ArrayList<>();
        folders.add(new File(bookImgDir));
        folders.add(new File(sheetFileDir));
        folders.add(new File(previewDir));

        log.info("========= Make UpFolder ==========");
        for (File folder : folders) {
            // 해당 디렉토리가 없다면 디렉토리를 생성.
            if (!folder.exists()) {
                try {
                    log.info("folder : {}", folder.getAbsolutePath());
                    boolean result = folder.mkdirs(); //폴더 생성합니다. ("새폴더"만 생성)
                    log.info("result : {}", result);
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
        }
    }
}

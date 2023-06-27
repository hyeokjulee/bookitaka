package com.bookitaka.NodeulProject.sheet;

public class UploadFile {

    String uuid;
    String fileName;

    public UploadFile(String uuid, String fileName) {
        this.uuid = uuid;
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}

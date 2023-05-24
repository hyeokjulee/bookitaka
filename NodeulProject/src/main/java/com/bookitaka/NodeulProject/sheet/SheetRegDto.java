package com.bookitaka.NodeulProject.sheet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SheetRegDto {

    @NotBlank
    private String sheetBooktitle;

    @NotBlank
    private String sheetBookauthor;

    @NotBlank
    private String sheetBookpublisher;
    private String sheetBookisbn;

    @NotNull
    private Integer sheetPrice;

    private String sheetBookimgname;

    @NotNull
    private String sheetFilename;

    @NotNull
    private String sheetGenreName;

    private String sheetAgegroupName;

    private String sheetContent;

    //미리보기 여부 체크하기로 했는디...
}

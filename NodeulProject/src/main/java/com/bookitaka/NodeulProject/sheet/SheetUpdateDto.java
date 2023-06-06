package com.bookitaka.NodeulProject.sheet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SheetUpdateDto {

    @NotBlank(message = "책 제목을 입력해주세요")
    private String sheetBooktitle;

    @NotBlank(message = "작가를 입력해주세요")
    private String sheetBookauthor;

    @NotBlank(message = "출판사를 입력해주세요")
    private String sheetBookpublisher;
    private String sheetBookisbn;

    @NotNull(message = "가격을 설정해주세요")
    @Range(min = 100, max = 100000, message = "가격이 100이하이거나 100000이상입니다. 확인해주세요.")
    private Integer sheetPrice;

    private String sheetBookimguuid;

    private String sheetBookimgname;

    private String sheetFileuuid;

    private String sheetFilename;

    @NotNull(message = "장르를 설정해주세요.")
    private String sheetGenreName;

    private String sheetAgegroupName;

    private String sheetContent;

}

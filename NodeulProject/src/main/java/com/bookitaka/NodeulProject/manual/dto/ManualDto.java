package com.bookitaka.NodeulProject.manual.dto;

import com.bookitaka.NodeulProject.manual.domain.entity.Manual;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class ManualDto {

    private Integer manualNo;
    @NotBlank(message = "제목을 입력해주세요.")
    private String manualTitle;
    @NotBlank(message = "내용은 입력해주세요.")
    private String manualContent;


    public Manual toEntity() {
        Manual manual = Manual.builder()
                .manualNo(manualNo)
                .manualTitle(manualTitle)
                .manualContent(manualContent)
                .build();
        return manual;
    }
    @Builder
    public ManualDto(Integer manualNo, String manualContent, String manualTitle) {
        this.manualNo = manualNo;
        this.manualContent = manualContent;
        this.manualTitle = manualTitle;
    }
}

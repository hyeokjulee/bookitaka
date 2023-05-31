package com.bookitaka.NodeulProject.manual.dto;

import com.bookitaka.NodeulProject.manual.domain.entity.Manual;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ManualDto {

    private Integer manualNo;
    private String manualTitle;
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

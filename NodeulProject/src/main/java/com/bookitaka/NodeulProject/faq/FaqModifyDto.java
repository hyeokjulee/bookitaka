package com.bookitaka.NodeulProject.faq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FaqModifyDto {

    private Long faqNo;

    @NotBlank
    private String faqQuestion;

    @NotBlank
    private String faqAnswer;

    @NotNull
    private String faqCategory;

    @NotNull
    private int faqBest;

}

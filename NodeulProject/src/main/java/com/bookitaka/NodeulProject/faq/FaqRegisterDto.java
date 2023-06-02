package com.bookitaka.NodeulProject.faq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FaqRegisterDto {

    @NotBlank
    private String faqQuestion;

    @NotBlank
    private String faqAnswer;

    @NotBlank
    private String faqCategory;

    @NotBlank
    private int faqBest;

}

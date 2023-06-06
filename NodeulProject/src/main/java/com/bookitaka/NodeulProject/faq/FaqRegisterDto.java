package com.bookitaka.NodeulProject.faq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FaqRegisterDto {

    @NotBlank(message = "질문은 필수입니다.")
    private String faqQuestion;

    @NotBlank(message = "답변은 필수입니다.")
    private String faqAnswer;

    @NotNull
    private String faqCategory;

    @NotNull
    private int faqBest;

}

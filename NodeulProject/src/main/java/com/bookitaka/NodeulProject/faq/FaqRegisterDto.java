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

    @NotBlank(message = "질문을 입력주세요.")
    private String faqQuestion;

    @NotBlank(message = "답변을 입력해주세요.")
    private String faqAnswer;

    @NotNull(message = "카테고리를 선택해주세요.")
    private String faqCategory;

    @NotNull(message = "답변을 입력해주세요.")
    private int faqBest;

}

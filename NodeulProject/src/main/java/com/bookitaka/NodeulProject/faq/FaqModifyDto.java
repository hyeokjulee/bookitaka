package com.bookitaka.NodeulProject.faq;

import com.bookitaka.NodeulProject.member.validation.Password;
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

    @NotBlank(message = "질문을 입력주세요.")
    private String faqQuestion;

    @NotBlank(message = "답변을 입력해주세요.")
    private String faqAnswer;

    @NotNull
    private String faqCategory;

    @NotNull
    private int faqBest;

}

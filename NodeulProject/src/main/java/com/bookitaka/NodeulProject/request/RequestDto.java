package com.bookitaka.NodeulProject.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto {

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "이메일 형식이 아닙니다.")
    private String requestEmail;

    @NotBlank(message = "이름을 입력해주세요.")
    private String requestName;

    private String requestPhone;
    private String requestBookisbn;

    @NotBlank(message = "도서 정보는 비워둘 수 없습니다. 도서를 검색해주세요.")
    private String requestBooktitle;
    private String requestBookauthor;
    private String requestBookpublisher;
    private String requestContent;
    private String requestHopedate;

}

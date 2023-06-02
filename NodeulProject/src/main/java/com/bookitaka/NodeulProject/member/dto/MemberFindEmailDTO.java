package com.bookitaka.NodeulProject.member.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
@Data
public class MemberFindEmailDTO {
    @NotBlank(message = "이름을 입력해 주세요.")
    @ApiModelProperty(position = 2)
    private String memberName;
    @ApiModelProperty(position = 5)
    @NotBlank(message = "생년월일을 입력해주세요")
    private String memberBirthday;
}

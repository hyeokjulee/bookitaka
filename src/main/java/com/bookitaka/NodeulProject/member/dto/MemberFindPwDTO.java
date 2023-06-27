package com.bookitaka.NodeulProject.member.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class MemberFindPwDTO {
    @NotBlank(message = "이메일을 입력해주세요.")
    @Pattern(regexp = "^(?:[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})?$", message = "이메일 형식이 맞는지 확인해 주세요.")
    @ApiModelProperty(position = 1)
    private String memberEmail;
    @NotBlank(message = "이름을 입력해주세요.")
    @ApiModelProperty(position = 2)
    private String memberName;
}

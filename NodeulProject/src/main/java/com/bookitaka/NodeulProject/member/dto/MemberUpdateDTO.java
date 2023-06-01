package com.bookitaka.NodeulProject.member.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class MemberUpdateDTO {
  @ApiModelProperty(position = 1)
  private String memberEmail;

  @NotBlank(message = "이름을 입력해 주세요.")
  @ApiModelProperty(position = 2)
  private String memberName;

  @NotBlank(message = "핸드폰 번호를 입력해 주세요.")
  @Pattern(regexp = "^(01[0-9]{1})(\\d{3}|\\d{4})(\\d{4})$", message = "올바른 휴대폰 번호를 입력해주세요.")
  @ApiModelProperty(position = 3)
  private String memberPhone;

  @ApiModelProperty(position = 4)
  private String memberGender;

  @ApiModelProperty(position = 5)
  @NotBlank(message = "생년월일을 입력해 주세요.")
  @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "생년월일을 형식에 맞게 입력해 주세요.")
  private String memberBirthday;
}

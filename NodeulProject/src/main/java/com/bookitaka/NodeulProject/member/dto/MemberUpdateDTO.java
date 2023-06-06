package com.bookitaka.NodeulProject.member.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class MemberUpdateDTO {

  @NotBlank(message = "이름을 입력해 주세요.")
  @ApiModelProperty(position = 1)
  private String memberName;

  @NotBlank(message = "핸드폰 번호를 입력해 주세요.")
  @Pattern(regexp = "^(\\d{3}-\\d{4}-\\d{4}|\\d{2}-\\d{4}-\\d{4}|\\d{2}-\\d{3}-\\d{4}|\\d{3}-\\d{3}-\\d{4}|)$", message = "전화번호 형식이 맞는지 확인해 주세요.")
  @ApiModelProperty(position = 2)
  private String memberPhone;

  @Pattern(regexp = "^(남성|여성|)$", message = "성별 형식이 맞는지 확인해 주세요.")
  @ApiModelProperty(position = 3)
  private String memberGender;

  @NotBlank(message = "생일을 입력해 주세요.")
  @Pattern(regexp = "^(?:\\d{4}-\\d{2}-\\d{2}|)$", message = "날짜 형식이 맞는지 확인해 주세요.")
  @ApiModelProperty(position = 4)
  private String memberBirthday;
}

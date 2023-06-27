package com.bookitaka.NodeulProject.member.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Data
public class MemberUpdateAdminDTO {

  @NotBlank(message = "이메일을 입력해주세요.")
  @Email(message = "올바른 이메일 형식이 아닙니다.")
  private String memberEmail;
  @NotBlank(message = "이름을 입력해주세요.")
  @ApiModelProperty(position = 1)
  private String memberName;

  @NotBlank(message = "핸드폰 번호를 입력해주세요.")
  @Pattern(regexp = "^(\\d{3}-\\d{4}-\\d{4}|\\d{2}-\\d{4}-\\d{4}|\\d{2}-\\d{3}-\\d{4}|\\d{3}-\\d{3}-\\d{4}|)$", message = "전화번호 형식이 맞는지 확인해 주세요.")
  @ApiModelProperty(position = 2)
  private String memberPhone;

  @Pattern(regexp = "^(남성|여성|)$", message = "성별 형식이 맞는지 확인해 주세요.")
  @ApiModelProperty(position = 3)
  private String memberGender;

  @NotBlank(message = "생일을 입력해주세요.")
  @Pattern(regexp = "^(?:\\d{4}-\\d{2}-\\d{2}|)$", message = "날짜 형식이 맞는지 확인해 주세요.")
  @ApiModelProperty(position = 4)
  private String memberBirthday;

  @Pattern(regexp = "^ROLE_(MEMBER|ADMIN)$", message = "권한 형식이 맞는지 확인해 주세요.")
  private String memberRole;
}

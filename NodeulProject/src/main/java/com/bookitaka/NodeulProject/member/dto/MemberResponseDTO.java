package com.bookitaka.NodeulProject.member.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Data
public class MemberResponseDTO {

  @ApiModelProperty(position = 0)
  @NotBlank(message = "이메일을 입력해주세요.")
  @Email(message = "올바른 이메일 형식이 아닙니다.")
  private String memberEmail;
  @ApiModelProperty(position = 1)
  @NotBlank(message = "이름을 입력해주세요.")
  private String memberName;
  @ApiModelProperty(position = 2)
  @NotBlank(message = "핸드폰 번호를 입력해주세요.")
  @Pattern(regexp = "^(01[0-9]{1})(\\d{3}|\\d{4})(\\d{4})$", message = "올바른 휴대폰 번호를 입력해주세요.")
  private String memberPhone;
  @ApiModelProperty(position = 3)
  private String memberGender;
  @ApiModelProperty(position = 4)
  @NotBlank(message = "생년월일을 입력해주세요.")
  @Pattern(regexp = "^\\d{8}$", message = "생년월일은 8자리 숫자로 입력해주세요 (예: 19910101)")
  private Date memberBirthday;
}

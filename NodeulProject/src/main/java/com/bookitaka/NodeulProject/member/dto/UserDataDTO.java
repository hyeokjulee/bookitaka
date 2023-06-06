package com.bookitaka.NodeulProject.member.dto;

import com.bookitaka.NodeulProject.member.validation.Password;
import com.bookitaka.NodeulProject.member.validation.PasswordMatch;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@PasswordMatch(password = "memberPassword", passwordCheck = "memberPasswordCheck")
public class UserDataDTO {
  @NotBlank(message = "이메일을 입력해 주세요.")
  @Pattern(regexp = "^(?:[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})?$", message = "이메일 형식이 맞는지 확인해 주세요.")
  @ApiModelProperty(position = 1)
  private String memberEmail;

  @NotBlank(message = "비밀번호를 입력해 주세요.")
  @Password(passwordPatterns = { "^.{8,}$|^.{0,0}$", "^(?=.*[A-Za-z]).*$", "^(?=.*\\d).*$", "^(?=.*[@$!%*?&.]).*$" },
          passwordMessages = { "비밀번호는 최소 8자 이상이어야 합니다.", "영문자", "숫자", "특수문자" })
  @ApiModelProperty(position = 2)
  private String memberPassword;

  @NotBlank(message = "비밀번호(확인)를 입력해 주세요.")
  @ApiModelProperty(position = 3)
  private String memberPasswordCheck;

  @NotBlank(message = "이름을 입력해 주세요.")
  @ApiModelProperty(position = 4)
  private String memberName;

  @NotBlank(message = "핸드폰 번호를 입력해 주세요.")
  @Pattern(regexp = "^(\\d{3}-\\d{4}-\\d{4}|\\d{2}-\\d{4}-\\d{4}|\\d{2}-\\d{3}-\\d{4}|\\d{3}-\\d{3}-\\d{4}|)$", message = "전화번호 형식이 맞는지 확인해 주세요.")
  @ApiModelProperty(position = 5)
  private String memberPhone;

  @Pattern(regexp = "^(남성|여성|)$", message = "성별 형식이 맞는지 확인해 주세요.")
  @ApiModelProperty(position = 6)
  private String memberGender;

  @NotBlank(message = "생일을 입력해 주세요.")
  @Pattern(regexp = "^(?:\\d{4}-\\d{2}-\\d{2}|)$", message = "날짜 형식이 맞는지 확인해 주세요.")
  @ApiModelProperty(position = 7)
  private String memberBirthday;
}

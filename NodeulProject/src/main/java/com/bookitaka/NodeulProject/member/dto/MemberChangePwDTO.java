package com.bookitaka.NodeulProject.member.dto;

import com.bookitaka.NodeulProject.member.validation.Password;
import com.bookitaka.NodeulProject.member.validation.PasswordMatch;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@PasswordMatch(password = "newMemberPassword", passwordCheck = "newMemberPasswordCheck")
public class MemberChangePwDTO {

  @ApiModelProperty(position = 0)
  @NotBlank(message = "비밀번호를 입력해 주세요.")
  @Password(passwordPatterns = { "^.{8,}$|^.{0,0}$", "^(?=.*[A-Za-z]).*$", "^(?=.*\\d).*$", "^(?=.*[@$!%*?&.]).*$" },
          passwordMessages = { "비밀번호는 최소 8자 이상이어야 합니다.", "영문자", "숫자", "특수문자" })
  private String oldMemberPassword;
  @ApiModelProperty(position = 1)
  @NotBlank(message = "비밀번호를 입력해 주세요.")
  @Password(passwordPatterns = { "^.{8,}$|^.{0,0}$", "^(?=.*[A-Za-z]).*$", "^(?=.*\\d).*$", "^(?=.*[@$!%*?&.]).*$" },
          passwordMessages = { "비밀번호는 최소 8자 이상이어야 합니다.", "영문자", "숫자", "특수문자" })
  private String newMemberPassword;
  @ApiModelProperty(position = 2)
  @NotBlank(message = "비밀번호(확인)를 입력해 주세요.")
  private String newMemberPasswordCheck;
}

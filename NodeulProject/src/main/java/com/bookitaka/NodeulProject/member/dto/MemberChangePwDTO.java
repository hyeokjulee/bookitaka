package com.bookitaka.NodeulProject.member.dto;

import com.bookitaka.NodeulProject.member.validation.PasswordMatch;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@PasswordMatch(password = "newMemberPassword", passwordCheck = "newMemberPasswordCheck")
public class MemberChangePwDTO {

  @ApiModelProperty(position = 0)
  @NotBlank(message = "비밀번호를 입력해주세요.")
  @Pattern(regexp="(?=.*[0-9])(?=.*[a-z])(?=.*\\W)(?=\\S+$).{6,12}",
          message = "비밀번호는 영문자와 숫자, 특수기호가 적어도 1개 이상 포함된 6자~12자의 비밀번호여야 합니다.")
  private String oldMemberPassword;
  @ApiModelProperty(position = 1)
  @NotBlank(message = "새 비밀번호를 입력해주세요.")
  @Pattern(regexp="(?=.*[0-9])(?=.*[a-z])(?=.*\\W)(?=\\S+$).{6,12}",
          message = "비밀번호는 영문자와 숫자, 특수기호가 적어도 1개 이상 포함된 6자~12자의 비밀번호여야 합니다.")
  private String newMemberPassword;
  @ApiModelProperty(position = 2)
  @NotBlank(message = "새 비밀번호를 입력해주세요.")
  @Pattern(regexp="(?=.*[0-9])(?=.*[a-z])(?=.*\\W)(?=\\S+$).{6,12}",
          message = "비밀번호는 영문자와 숫자, 특수기호가 적어도 1개 이상 포함된 6자~12자의 비밀번호여야 합니다.")
  private String newMemberPasswordCheck;
}

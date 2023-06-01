package com.bookitaka.NodeulProject.member.dto;

import com.bookitaka.NodeulProject.member.validation.PasswordMatch;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@PasswordMatch(passwordField = "memberPassword", passwordConfirmField = "memberPasswordCheck")
public class UserDataDTO {

  @ApiModelProperty(position = 0)
  @NotBlank(message = "이메일을 입력해주세요.")
  @Email
  private String memberEmail;
  @ApiModelProperty(position = 1)
  @NotBlank(message = "비밀번호를 입력해주세요.")
  @Pattern(regexp="(?=.*[0-9])(?=.*[a-z])(?=.*\\W)(?=\\S+$).{6,12}",
          message = "비밀번호는 영문자와 숫자, 특수기호가 적어도 1개 이상 포함된 6자~12자의 비밀번호여야 합니다.")
  private String memberPassword;
  @ApiModelProperty(position = 2)
  @NotBlank(message = "비밀번호를 입력해주세요.")
  @Pattern(regexp="(?=.*[0-9])(?=.*[a-z])(?=.*\\W)(?=\\S+$).{6,12}",
          message = "비밀번호는 영문자와 숫자, 특수기호가 적어도 1개 이상 포함된 6자~12자의 비밀번호여야 합니다.")
  private String memberPasswordCheck;
  @ApiModelProperty(position = 3)
  @NotBlank(message = "이름을 입력해주세요.")
  private String memberName;
  @ApiModelProperty(position = 4)
  @NotBlank(message = "전화번호를 입력해주세요.")
  private String memberPhone;
  @ApiModelProperty(position = 5)
  private String memberGender;
//  @ApiModelProperty(position = 5)
//  private Date memberBirthday;
  @ApiModelProperty(position = 6)
  private String memberRole;
}

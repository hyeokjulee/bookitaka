package com.bookitaka.NodeulProject.member.bstest.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class UserDataDTO {

  @ApiModelProperty(position = 0)
  private String memberEmail;
  @ApiModelProperty(position = 1)
  private String memberPassword;
  @ApiModelProperty(position = 2)
  private String memberName;
  @ApiModelProperty(position = 3)
  private String memberPhone;
  @ApiModelProperty(position = 4)
  private String memberGender;
  @ApiModelProperty(position = 5)
  private Date memberBirthday;
  @ApiModelProperty(position = 6)
  private String memberRole;

}

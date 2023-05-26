package com.bookitaka.NodeulProject.member.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class MemberChangePwDTO {

  @ApiModelProperty(position = 0)
  private String oldPw;
  @ApiModelProperty(position = 1)
  private String newPw;
  @ApiModelProperty(position = 2)
  private String newPwChk;
}

package com.bookitaka.NodeulProject.member.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class MemberChangePwDTO {

  @ApiModelProperty(position = 0)
  private String memberPassword;
}

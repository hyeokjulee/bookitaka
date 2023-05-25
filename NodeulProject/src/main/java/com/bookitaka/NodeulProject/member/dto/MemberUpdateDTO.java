package com.bookitaka.NodeulProject.member.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class MemberUpdateDTO {

  @ApiModelProperty(position = 0)
  private String memberName;
  @ApiModelProperty(position = 1)
  private String memberPhone;
  @ApiModelProperty(position = 2)
  private String memberGender;
  @ApiModelProperty(position = 3)
  private Date memberBirthday;
}

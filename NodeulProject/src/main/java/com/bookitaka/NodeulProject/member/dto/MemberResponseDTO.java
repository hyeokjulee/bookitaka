package com.bookitaka.NodeulProject.member.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class MemberResponseDTO {

  @ApiModelProperty(position = 0)
  private String memberEmail;
  @ApiModelProperty(position = 1)
  private String memberName;
  @ApiModelProperty(position = 2)
  private String memberPhone;
  @ApiModelProperty(position = 3)
  private String memberGender;
  @ApiModelProperty(position = 4)
  private String memberBirthday;
  @ApiModelProperty(position = 5)
  private String memberRole;
  @ApiModelProperty(position = 6)
  private Date memberJoindate;

}

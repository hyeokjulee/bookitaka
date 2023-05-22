package com.bookitaka.NodeulProject.member.bstest.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class UserResponseDTO {

  @ApiModelProperty(position = 0)
  private Integer memberEmail;
  @ApiModelProperty(position = 1)
  private String memberName;
  @ApiModelProperty(position = 2)
  private String memberGender;
  @ApiModelProperty(position = 3)
  private String memberRole;

}

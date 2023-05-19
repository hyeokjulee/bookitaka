package com.bookitaka.NodeulProject.member.bstest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id @GeneratedValue
    private Integer memberNo;

    private String memberEmail;

    private String memberPassword;

    private String memberName;

    private String memberPhone;

    private String memberGender;

    private Date memberBirthday;

    private String memberRole;
}

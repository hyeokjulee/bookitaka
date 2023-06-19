package com.bookitaka.NodeulProject.member.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
public class Member implements Serializable {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer memberNo;

    private String memberEmail;

    private String memberPassword;

    private String memberName;

    private String memberPhone;

    private String memberGender;

    private String memberBirthday;

    private String memberRole;

    private String memberRtoken;

    private Date memberJoindate;
}

package com.bookitaka.NodeulProject.payment;

import com.bookitaka.NodeulProject.member.model.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
public class Payment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentNo;

    private String paymentUuid;
    private String paymentInfo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn (name = "memberEmail", referencedColumnName = "memberEmail")
    private Member member;

    private Long paymentPrice;

    private Date paymentDate;

}

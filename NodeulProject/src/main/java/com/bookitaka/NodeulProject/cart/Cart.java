package com.bookitaka.NodeulProject.cart;

import com.bookitaka.NodeulProject.member.model.Member;
import com.bookitaka.NodeulProject.sheet.Sheet;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@DynamicInsert
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_no")
    private int cartNo;

    @ManyToOne
    @JoinColumn(name = "member_email")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "sheet_no")
    private Sheet sheet;

    @Column(name = "cart_regdate", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime cartRegdate;
}
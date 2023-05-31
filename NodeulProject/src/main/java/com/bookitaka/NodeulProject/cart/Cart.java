package com.bookitaka.NodeulProject.cart;

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

    @Column(name = "member_email")
    private String memberEmail;

    @Column(name = "sheet_no")
    private int sheetNo;

    @Column(name = "cart_regdate", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime cartRegdate;
}
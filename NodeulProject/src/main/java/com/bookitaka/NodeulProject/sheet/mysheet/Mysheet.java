package com.bookitaka.NodeulProject.sheet.mysheet;

import com.bookitaka.NodeulProject.member.model.Member;
import com.bookitaka.NodeulProject.sheet.Sheet;
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
public class Mysheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mysheetNo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn (name = "memberEmail", referencedColumnName = "memberEmail")
    private Member member;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn (name = "sheetNo")
    private Sheet sheet;

    private String mysheetMeans;

    private Date mysheetStartdate;
    private Date mysheetEnddate;

}

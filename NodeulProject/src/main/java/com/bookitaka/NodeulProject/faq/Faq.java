package com.bookitaka.NodeulProject.faq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
public class Faq {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long faqNo;

    private String faqQuestion;
    private String faqAnswer;
    private String faqCategory;
    private int faqBest;

    private Date faqRegdate;
    private Date faqModdate;

}

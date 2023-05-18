package com.bookitaka.NodeulProject.faq;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Getter
@Setter
public class Faq {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private int faqNo;

    private String faqQuestion;

    private String faqAnswer;

    private String faq_category;

    private int faqBest;

    private Date faqModdate;

}

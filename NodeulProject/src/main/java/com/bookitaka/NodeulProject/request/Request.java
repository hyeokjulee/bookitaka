package com.bookitaka.NodeulProject.request;

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
public class Request {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestNo;

    private String requestEmail;
    private String requestName;
    private String requestPhone;

    private String requestBookisbn;
    private String requestBooktitle;
    private String requestBookauthor;
    private String requestBookpublisher;
    private String requestContent;
    private int requestIsdone;
    private String requestHopedate;

    private Date requestRegdate;
}

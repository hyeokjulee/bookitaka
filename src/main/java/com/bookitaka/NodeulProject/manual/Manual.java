package com.bookitaka.NodeulProject.manual;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Manual {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer manualNo;

    private String manualTitle;

    private String manualContent;

    @Builder
    public Manual(Integer manualNo,String manualTitle, String manualContent) {
        this.manualNo = manualNo;
        this.manualTitle = manualTitle;
        this.manualContent = manualContent;
    }
}

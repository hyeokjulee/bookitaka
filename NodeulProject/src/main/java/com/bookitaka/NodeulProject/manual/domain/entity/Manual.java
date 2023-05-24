package com.bookitaka.NodeulProject.manual.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@DynamicInsert
public class Manual {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer manualNo;
    private String manualTitle;
    private String manualContent;

    @Builder
    public Manual(Integer manualNo, String manualTitle, String manualContent) {
        this.manualNo = manualNo;
        this.manualTitle = manualTitle;
        this.manualContent = manualContent;
    }
}

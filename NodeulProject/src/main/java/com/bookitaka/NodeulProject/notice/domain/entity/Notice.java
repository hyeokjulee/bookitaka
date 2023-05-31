package com.bookitaka.NodeulProject.notice.domain.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Notice extends TimeEntity{

    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer noticeNo;
    private String noticeTitle;
    private String noticeContent;
    private Integer noticeHit;


    @Builder
    public Notice(Integer noticeNo, String noticeTitle, String noticeContent, Integer noticeHit) {
        this.noticeNo = noticeNo;
        this.noticeTitle = noticeTitle;
        this.noticeContent = noticeContent;
        this.noticeHit = noticeHit;
    }

    public void update(String noticeTitle, String noticeContent){
        this.noticeTitle = noticeTitle;
        this.noticeContent = noticeContent;

    }

}

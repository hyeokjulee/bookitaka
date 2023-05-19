package com.bookitaka.NodeulProject.notice.domain.entity;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "notice")
public class NoticeEntity extends TimeEntity{

    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer noticeNo;

    @Column(length = 100, nullable = false)
    private String noticeTitle;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String noticeContent;



    @Builder
    public NoticeEntity(Integer noticeNo, String noticeTitle, String noticeContent) {
        this.noticeNo = noticeNo;
        this.noticeTitle = noticeTitle;
        this.noticeContent = noticeContent;
    }
}

package com.bookitaka.NodeulProject.notice.domain.entity;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "notice")
public class NoticeEntity extends TimeEntity{

    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer notice_no;

    @Column(length = 100, nullable = false)
    private String notice_title;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String notice_content;



    @Builder
    public NoticeEntity(Integer notice_no, String notice_title, String notice_content) {
        this.notice_no = notice_no;
        this.notice_title = notice_title;
        this.notice_content = notice_content;
    }
}

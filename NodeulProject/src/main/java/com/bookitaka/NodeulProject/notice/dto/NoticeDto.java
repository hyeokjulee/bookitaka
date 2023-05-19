package com.bookitaka.NodeulProject.notice.dto;

import com.bookitaka.NodeulProject.notice.domain.entity.NoticeEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class NoticeDto {
    private Integer noticeNo;
    private String noticeTitle;
    private String noticeContent;
    private Integer noticeHit;
    private LocalDateTime noticeRegdate;
    private LocalDateTime noticeModdate;

    public NoticeEntity toEntity(){
        NoticeEntity noticeEntity= NoticeEntity.builder()
                .noticeNo(noticeNo)
                .noticeTitle(noticeTitle)
                .noticeContent(noticeContent)
                .build();
        return noticeEntity;
    }

    @Builder
    public NoticeDto(Integer noticeNo, String noticeTitle, String noticeContent, LocalDateTime noticeRegdate, LocalDateTime noticeModdate) {
        this.noticeNo = noticeNo;
        this.noticeTitle = noticeTitle;
        this.noticeContent = noticeContent;
        this.noticeRegdate = noticeRegdate;
        this.noticeModdate = noticeModdate;
    }
}

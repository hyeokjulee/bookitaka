package com.bookitaka.NodeulProject.notice.dto;

import com.bookitaka.NodeulProject.notice.domain.entity.NoticeEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class NoticeDto {
    private Integer notice_no;
    private String notice_title;
    private String notice_content;
    private Integer notice_hit;
    private LocalDateTime notice_regdate;
    private LocalDateTime notice_moddate;

    public NoticeEntity toEntity(){
        NoticeEntity noticeEntity= NoticeEntity.builder()
                .notice_no(notice_no)
                .notice_title(notice_title)
                .notice_content(notice_content)
                .build();
        return noticeEntity;
    }

    @Builder
    public NoticeDto(Integer notice_no, String notice_title, String notice_content, LocalDateTime notice_regdate, LocalDateTime notice_moddate) {
        this.notice_no = notice_no;
        this.notice_title = notice_title;
        this.notice_content = notice_content;
        this.notice_regdate = notice_regdate;
        this.notice_moddate = notice_moddate;
    }
}

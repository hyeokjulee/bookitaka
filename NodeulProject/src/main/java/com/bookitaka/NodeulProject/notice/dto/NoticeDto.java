package com.bookitaka.NodeulProject.notice.dto;

import com.bookitaka.NodeulProject.notice.domain.entity.Notice;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class NoticeDto {
    private Integer noticeNo;
    @NotBlank(message = "제목은 필수입니다.")
    private String noticeTitle;
    private String noticeContent;
    private Integer noticeHit;
    private LocalDateTime noticeRegdate;
    private LocalDateTime noticeModdate;

    public Notice toEntity(){
        Notice notice = Notice.builder()
                .noticeNo(noticeNo)
                .noticeTitle(noticeTitle)
                .noticeContent(noticeContent)
                .noticeHit(0)
                .build();
        return notice;
    }

    @Builder
    public NoticeDto(Integer noticeNo, String noticeTitle, String noticeContent, Integer noticeHit, LocalDateTime noticeRegdate, LocalDateTime noticeModdate) {
        this.noticeNo = noticeNo;
        this.noticeTitle = noticeTitle;
        this.noticeContent = noticeContent;
        this.noticeHit = noticeHit;
        this.noticeRegdate = noticeRegdate;
        this.noticeModdate = noticeModdate;
    }
}

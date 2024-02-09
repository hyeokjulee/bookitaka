package com.bookitaka.NodeulProject.notice;

import com.bookitaka.NodeulProject.notice.Notice;
import com.bookitaka.NodeulProject.notice.NoticeDto;
import com.bookitaka.NodeulProject.notice.NoticeRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@AllArgsConstructor
@Service
public class NoticeService {

    private NoticeRepository noticeRepository;

    @Transactional
    //게시글 리스트 처리
    public Page<NoticeDto> getNoticeList(Pageable pageable, String keyword) {
        if (keyword != null) {
            Page<Notice> noticeEntities = noticeRepository.findByNoticeTitleContainingOrNoticeContentContaining(keyword, keyword, pageable);

            if (noticeEntities.isEmpty()) {
                return Page.empty(); // 빈 페이지 반환
            }

            return noticeEntities.map(this::convertEntityToDto);
        } else {
            Page<Notice> noticeEntities = noticeRepository.findAll(pageable);
            return noticeEntities.map(this::convertEntityToDto);
        }
    }

    /*게시글 생성*/
    @Transactional
    public Integer registerNotice(NoticeDto noticeDto){
        return noticeRepository.save(noticeDto.toEntity()).getNoticeNo();
    }

    @Transactional
    public NoticeDto getNotice(Integer noticeNo) {
        Optional<Notice> noticeEntityWrapper = noticeRepository.findById(noticeNo);
        Notice notice = noticeEntityWrapper.get();

        NoticeDto noticeDto = NoticeDto.builder()
                .noticeNo(notice.getNoticeNo())
                .noticeTitle(notice.getNoticeTitle())
                .noticeHit(notice.getNoticeHit())
                .noticeContent(notice.getNoticeContent())
                .noticeRegdate(notice.getNoticeRegdate())
                .noticeModdate(notice.getNoticeModdate())
                .build();

        return noticeDto;
    }

    @Transactional
    public Notice updateNotice(Integer noticeNo, Notice notice) {
        Notice noticeData= noticeRepository.findById(noticeNo).orElseThrow(IllegalArgumentException::new);;
        noticeData.update(notice.getNoticeTitle(),notice.getNoticeContent());
        return noticeData;
    }

    @Transactional
    public void removeNotice(Integer noticeNo) {
        noticeRepository.deleteById(noticeNo);
    }

    @Transactional    //조회수
    public int updateHit(Integer noticeNo) {
        return noticeRepository.updateHit(noticeNo);
    }

    private NoticeDto convertEntityToDto(Notice notice) {
        return NoticeDto.builder()
                .noticeNo(notice.getNoticeNo())
                .noticeTitle(notice.getNoticeTitle())
                .noticeContent(notice.getNoticeContent())
                .noticeHit(notice.getNoticeHit())
                .noticeRegdate(notice.getNoticeRegdate())
                .build();

    }

}

package com.bookitaka.NodeulProject.notice.service;

import com.bookitaka.NodeulProject.notice.domain.entity.Notice;
import com.bookitaka.NodeulProject.notice.dto.NoticeDto;
import com.bookitaka.NodeulProject.notice.repository.NoticeRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class NoticeService {

    private NoticeRepository noticeRepository;

    @Transactional
    //게시글 리스트 처리
    public List<NoticeDto> getNoticeList(){
        List<Notice> noticeEntities = noticeRepository.findAll(Sort.by(Sort.Direction.DESC, "noticeNo"));
        List<NoticeDto> noticeDtoList = new ArrayList<>();

        for (Notice notice : noticeEntities){
            NoticeDto noticeDto = NoticeDto.builder()
                    .noticeNo(notice.getNoticeNo())
                    .noticeTitle(notice.getNoticeTitle())
                    .noticeContent(notice.getNoticeContent())
                    .noticeHit(notice.getNoticeHit())
                    .noticeRegdate(notice.getNoticeRegdate())
                    .build();

            noticeDtoList.add(noticeDto);
        }
        return  noticeDtoList;
    }

    @Transactional //페이징
    public Page<Notice> pageList(Pageable pageable) {
        return noticeRepository.findAll(pageable);
    }

    /*게시글 생성*/
    @Transactional
    public Integer savePost(NoticeDto noticeDto){
        return noticeRepository.save(noticeDto.toEntity()).getNoticeNo();
    }

    @Transactional
    public NoticeDto getPost(Integer noticeNo) {
        Optional<Notice> noticeEntityWrapper = noticeRepository.findById(noticeNo);
        Notice notice = noticeEntityWrapper.get();

        NoticeDto noticeDto = NoticeDto.builder()
                .noticeNo(notice.getNoticeNo())
                .noticeTitle(notice.getNoticeTitle())
                .noticeHit(notice.getNoticeHit())
                .noticeContent(notice.getNoticeContent())
                .noticeRegdate(notice.getNoticeRegdate())
                .build();

        return noticeDto;
    }

    @Transactional
    public void deletePost(Integer noticeNo) {
        noticeRepository.deleteById(noticeNo);
    }

    @Transactional
    public List<NoticeDto> searchPost(String keyword) {
        List<Notice> noticeEntities = noticeRepository.findByNoticeTitleContaining(keyword,Sort.by(Sort.Direction.DESC, "noticeNo"));
        List<NoticeDto> noticeDtoList = new ArrayList<>();

        if (noticeEntities.isEmpty()) return noticeDtoList;

        for (Notice notice : noticeEntities) {
            noticeDtoList.add(this.convertEntityToDto(notice));
        }

        return noticeDtoList;
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

package com.bookitaka.NodeulProject.notice.service;

import com.bookitaka.NodeulProject.notice.dto.NoticeDto;
import com.bookitaka.NodeulProject.notice.repository.NoticeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class NoticeService {

    private NoticeRepository noticeRepository;

    /*게시글 생성*/
    @Transactional
    public Integer savePost(NoticeDto noticeDto){
        return noticeRepository.save(noticeDto.toEntity()).getNoticeNo();
    }
}

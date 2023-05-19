package com.bookitaka.NodeulProject.notice;

import com.bookitaka.NodeulProject.notice.domain.entity.NoticeEntity;
import com.bookitaka.NodeulProject.notice.repository.NoticeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class NoticeEntityTests {

    @Autowired
    NoticeRepository noticeRepository;

    @Test
    void save(){
        NoticeEntity params = NoticeEntity.builder()
                .notice_title("1번 게시글 제목")
                .notice_content("1번 게시글 내용")
                .build();

        noticeRepository.save(params);

        NoticeEntity entity = noticeRepository.findById((Integer) 1).get();
        Assertions.assertThat(entity.getNotice_title()).isEqualTo("1번 게시글 제목");
        Assertions.assertThat(entity.getNotice_content()).isEqualTo("1번 게시글 내용");
    }

    @Test
    void findAll(){
        long noticeCount = noticeRepository.count();
        List<NoticeEntity> notices = noticeRepository.findAll();
    }

    @Test
    void delete(){
        NoticeEntity entity = noticeRepository.findById((Integer) 1).get();
        noticeRepository.delete(entity);
    }
}

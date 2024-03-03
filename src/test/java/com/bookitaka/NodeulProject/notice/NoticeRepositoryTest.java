package com.bookitaka.NodeulProject.notice;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class NoticeRepositoryTest {

    @Autowired
    NoticeRepository noticeRepository;

    private final String noticeTitle = "1번 게시글 제목";
    private final String noticeContent = "1번 게시글 내용";
    private final Notice testNotice = Notice.builder().noticeTitle(noticeTitle).noticeContent(noticeContent).build();

    @BeforeEach
    void beforeTest() {
        noticeRepository.save(testNotice);
    }

    @AfterEach
    void afterTest() {
        noticeRepository.delete(testNotice);
    }

    @Test
    void save_findById(){
        Notice entity = noticeRepository.findByNoticeTitle(noticeTitle);

        assertThat(entity.getNoticeTitle()).isEqualTo(noticeTitle);
        assertThat(entity.getNoticeContent()).isEqualTo(noticeContent);
    }

    @Test
    void count_findAll(){
        long noticeCount = noticeRepository.count();
        Iterable<Notice> allNoticesIterable = noticeRepository.findAll();
        List<Notice> allNoticesList =
                StreamSupport.stream(allNoticesIterable.spliterator(), false).collect(Collectors.toList());

        assertThat(noticeCount).isEqualTo(allNoticesList.size());
    }

    @Test
    void delete(){
        long beforeNoticeCount = noticeRepository.count();

        Notice testNotice2 = Notice.builder().noticeTitle(noticeTitle + "2").noticeContent(noticeContent).build();
        noticeRepository.save(testNotice2);

        Notice entity = noticeRepository.findByNoticeTitle(noticeTitle + "2");
        noticeRepository.delete(entity);

        long afterNoticeCount = noticeRepository.count();
        assertThat(afterNoticeCount).isEqualTo(beforeNoticeCount);
    }
}

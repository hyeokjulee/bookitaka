//package com.bookitaka.NodeulProject.notice;
//
//import com.bookitaka.NodeulProject.notice.Notice;
//import com.bookitaka.NodeulProject.notice.NoticeRepository;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//
//@SpringBootTest
//public class NoticeEntityTests {
//
//    @Autowired
//    NoticeRepository noticeRepository;
//
//    @Test
//    void save(){
//        Notice params = Notice.builder()
//                .noticeTitle("1번 게시글 제목")
//                .noticeContent("1번 게시글 내용")
//                .build();
//
//        noticeRepository.save(params);
//
//        Notice entity = noticeRepository.findById((Integer) 1).get();
//        Assertions.assertThat(entity.getNoticeTitle()).isEqualTo("1번 게시글 제목");
//        Assertions.assertThat(entity.getNoticeContent()).isEqualTo("1번 게시글 내용");
//    }
//
//    @Test
//    void findAll(){
//        long noticeCount = noticeRepository.count();
//        List<Notice> notices = noticeRepository.findAll();
//    }
//
//    @Test
//    void delete(){
//        Notice entity = noticeRepository.findById((Integer) 1).get();
//        noticeRepository.delete(entity);
//    }
//}

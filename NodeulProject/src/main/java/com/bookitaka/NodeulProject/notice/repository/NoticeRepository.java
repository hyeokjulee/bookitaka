package com.bookitaka.NodeulProject.notice.repository;

import com.bookitaka.NodeulProject.notice.domain.entity.Notice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice,Integer> {

    @Modifying
    @Query("update Notice n set n.noticeHit = n.noticeHit + 1 where n.noticeNo = :noticeNo")
    int updateHit(Integer noticeNo);

    List<Notice> findByNoticeTitleContaining(String keyword, Sort noticeNo);

}

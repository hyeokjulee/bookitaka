package com.bookitaka.NodeulProject.notice.repository;

import com.bookitaka.NodeulProject.notice.domain.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice,Integer> {

    List<Notice> findByNoticeTitleContaining(String keyword);

}

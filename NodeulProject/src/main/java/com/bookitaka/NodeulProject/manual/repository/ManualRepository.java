package com.bookitaka.NodeulProject.manual.repository;

import com.bookitaka.NodeulProject.manual.domain.entity.Manual;
import com.bookitaka.NodeulProject.notice.domain.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ManualRepository extends PagingAndSortingRepository<Manual,Integer> {
    Page<Manual> findByManualTitleContainingOrManualContentContaining(String titleKeyword, String contentKeyword, Pageable pageable);
}

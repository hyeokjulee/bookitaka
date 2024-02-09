package com.bookitaka.NodeulProject.manual;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ManualRepository extends PagingAndSortingRepository<Manual,Integer> {
    Page<Manual> findByManualTitleContainingOrManualContentContaining(String titleKeyword, String contentKeyword, Pageable pageable);
}

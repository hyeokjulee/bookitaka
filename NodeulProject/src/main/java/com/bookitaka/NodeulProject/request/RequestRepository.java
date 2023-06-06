package com.bookitaka.NodeulProject.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RequestRepository extends PagingAndSortingRepository<Request, Long> {

    Page<Request> findAllByRequestIsdoneOrderByRequestRegdateDesc(int requestIsdone, Pageable pageable);

}

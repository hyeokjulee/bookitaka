package com.bookitaka.NodeulProject.request;

import com.bookitaka.NodeulProject.member.model.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RequestRepository extends PagingAndSortingRepository<Request, Long> {

    Page<Request> findAllByRequestIsdone(int requestIsdone, Pageable pageable);

    Page<Request> findAllByRequestEmail(String requestEmail, Pageable pageable);

}

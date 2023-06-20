package com.bookitaka.NodeulProject.request;

import com.bookitaka.NodeulProject.member.model.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface RequestRepository extends PagingAndSortingRepository<Request, Long> {

    Page<Request> findAllByRequestIsdone(int requestIsdone, Pageable pageable);

    Page<Request> findAllByRequestEmail(String requestEmail, Pageable pageable);

    @Modifying
    @Query("UPDATE Request r SET r.requestIsdone = :requestIsdone WHERE r.requestNo = :requestNo")
    int updateRequestIsdone(@Param("requestNo") Long requestNo, @Param("requestIsdone") int requestIsdone);

}

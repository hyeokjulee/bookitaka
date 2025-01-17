package com.bookitaka.NodeulProject.faq;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;


public interface FaqRepository extends PagingAndSortingRepository<Faq, Long> {

    Page<Faq> findAllByFaqCategory(String faqCategory, Pageable pageable);

    Page<Faq> findAllByFaqBest(int faqBest, Pageable pageable);

    Page<Faq> findAllByFaqQuestionContaining(String keyword, Pageable pageable);

//    List<Faq> findAll();

    Optional<Faq> findById(Long faqNo);
}

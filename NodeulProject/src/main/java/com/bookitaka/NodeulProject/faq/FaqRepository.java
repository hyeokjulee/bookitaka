package com.bookitaka.NodeulProject.faq;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


public interface FaqRepository extends PagingAndSortingRepository<Faq, Long> {

    Page<Faq> findAllByFaqCategoryOrderByFaqRegdateDesc(String faqCategory, Pageable pageable);
    Page<Faq> findAllByFaqBestOrderByFaqRegdateDesc(int faqBest, Pageable pageable);

//    Page<Faq> findAllByFaqAnswerContainsAndFaqQuestionContains(String keyword, Pageable pageable);
    Page<Faq> findAllByFaqQuestionContaining(String keyword, Pageable pageable);
//    Page<Faq> findAllByFaqQuestionOrFaqAnswerContaining(String keyword, Pageable pageable);



}

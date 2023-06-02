package com.bookitaka.NodeulProject.faq;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


public interface FaqRepository extends PagingAndSortingRepository<Faq, Long> {

//    Page<Faq> findAllByFaqCategory(String faqCategory, Pageable pageable);
    Page<Faq> findAllByFaqCategoryOrderByFaqRegdateDesc(String faqCategory, Pageable pageable);
//    Page<Faq> findAllByFaqBest(int faqBest, Pageable pageable);
    Page<Faq> findAllByFaqBestOrderByFaqRegdateDesc(int faqBest, Pageable pageable);

//    List<Faq> findAllByFaqCategory(String faqCategory);
//    List<Faq> findAllByFaqBest(int faqBest);

//    List<Faq> findByFaqQuestionAndFaqAnswerContaining();


}

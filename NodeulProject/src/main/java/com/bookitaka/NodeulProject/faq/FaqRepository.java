package com.bookitaka.NodeulProject.faq;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface FaqRepository extends JpaRepository<Faq, Long> {

    List<Faq> findAllByFaqCategory(String faqCategory);
    List<Faq> findAllByFaqBest(int faqBest);

    List<Faq> findByFaqQuestionAndFaqAnswerContaining();


}

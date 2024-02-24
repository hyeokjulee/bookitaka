package com.bookitaka.NodeulProject.faq;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface FaqService {

    boolean registerFaq(Faq faq);

//    List<Faq> getAllFaq();

    Optional<Faq> getOneFaq(Long faqNo);

    void modifyFaq(Faq faqModified);

    void removeFaq(Faq faq);

    long countFaq();

    List<String> getAllFaqCategory();

    Page<Faq> getAllFaqByFaqCategory(String faqCategory, Pageable pageable);

    Page<Faq> getAllFaqContaningKeyword(String keyword, Pageable pageable);
}
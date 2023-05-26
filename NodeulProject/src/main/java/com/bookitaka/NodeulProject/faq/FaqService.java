package com.bookitaka.NodeulProject.faq;

import java.util.List;
import java.util.Optional;

public interface FaqService {

    void registerFaq(Faq faq);
    List<Faq> getAllFaq();
    Optional<Faq> getOneFaq(Long faqNo);
    void modifyFaq(Long faqNo ,Faq newFaq);
    void removeFaq(Faq faq);
    long countFaq();

    List<String> getAllFaqCategory();
    List<Faq> getAllFaqByFaqCategory(String faqCategory);
    List<Faq> getAllFaqByFaqBest();

    void isbnSend(String keyword, String author);

}

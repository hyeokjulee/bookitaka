package com.bookitaka.NodeulProject.faq;

import java.util.List;
import java.util.Optional;

public interface FaqService {

    void registerFaq(Faq faq);
    List<Faq> getAllFaq();
    Optional<Faq> getOneFaq(Faq faq);
    void modifyFaq(Long faqNo ,Faq newFaq);
    void removeFaq(Faq faq);
    long countFaq();

    void isbnSend(String keyword, String author);

}

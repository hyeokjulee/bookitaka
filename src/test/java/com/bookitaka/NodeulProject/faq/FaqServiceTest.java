package com.bookitaka.NodeulProject.faq;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
//@Transactional
class FaqServiceTest {

    @Autowired
    FaqService faqService;

    private final String question = "question";
    private final String answer = "answer";
    private final String category = "category";
    private final String update = "update";
    private final int insertNum = 2;

    @Test
    public void registerFaq_getOneFaq_modifyFaq_countFaq_removeFaq() {
        // register
        Faq faq1 = new Faq();
        faq1.setFaqQuestion(question);
        faq1.setFaqAnswer(answer);
        faq1.setFaqCategory(category);
        faq1.setFaqBest(1);
        Faq faq2 = new Faq(null, question, answer, category, 0, null, null);
        faqService.registerFaq(faq1);
        faqService.registerFaq(faq2);

        // findOneFaq 단건 조회
        Faq findFaq1 = faqService.getOneFaq(faq1.getFaqNo()).get();
        Faq findFaq2 = faqService.getOneFaq(faq2.getFaqNo()).get();
        assertThat(findFaq1.getFaqQuestion()).isEqualTo(faq1.getFaqQuestion());
        assertThat(findFaq2.getFaqQuestion()).isEqualTo(faq2.getFaqQuestion());

        // modifyFaq 수정 검증
        Faq newFaq1 = new Faq();
        newFaq1.setFaqQuestion(update + " - " + question);
        newFaq1.setFaqAnswer(update + " - " + answer);
        newFaq1.setFaqCategory(update + " - " + category);
        newFaq1.setFaqBest(0);
        newFaq1.setFaqNo(findFaq1.getFaqNo());
        Faq newFaq2 = new Faq(findFaq2.getFaqNo(), update + " - " + question, update + " - " + answer,
                update + " - " + category, 1, null, null);

        faqService.modifyFaq(newFaq1);
        faqService.modifyFaq(newFaq2);

        Faq updateFaq1 = faqService.getOneFaq(findFaq1.getFaqNo()).get();
        Faq updateFaq2 = faqService.getOneFaq(findFaq2.getFaqNo()).get();

        System.out.println("faq1.getFaqNo() : " + faq1.getFaqNo());
        System.out.println("faq1.getFaqQuestion() : " + faq1.getFaqQuestion());
        System.out.println("findFaq1.getFaqNo() : " + findFaq1.getFaqNo());
        System.out.println("findFaq1.getFaqQuestion() : " + findFaq1.getFaqQuestion());
        System.out.println("newFaq1.getFaqNo() : " + newFaq1.getFaqNo());
        System.out.println("newFaq1.getFaqQuestion() : " + newFaq1.getFaqQuestion());
        System.out.println("updateFaq1.getFaqNo() : " + updateFaq1.getFaqNo());
        System.out.println("updateFaq1.getFaqQuestion() : " + updateFaq1.getFaqQuestion());

        assertThat(updateFaq1.getFaqQuestion()).isEqualTo(newFaq1.getFaqQuestion());
        assertThat(updateFaq2.getFaqQuestion()).isEqualTo(newFaq2.getFaqQuestion());

        // removeFaq 삭제 검증
        long beforeCount = faqService.countFaq();
        faqService.removeFaq(faq1);
        faqService.removeFaq(faq2);
        long deletedcount = faqService.countFaq();
        assertThat(deletedcount).isEqualTo(beforeCount - insertNum);
    }

//    @Test
//    public void apiKeyTest() {
//        String keyword = "토지";
//        String author = "박경리";
//        service.isbnSend(keyword, author);
//    }
}
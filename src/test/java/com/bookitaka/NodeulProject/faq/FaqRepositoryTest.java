package com.bookitaka.NodeulProject.faq;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class FaqRepositoryTest {

    @Autowired
    FaqRepository faqRepository;

    final String question = "question";
    final String answer = "answer";
    final String category = "category";
    final String update = "update";
    final int insertNum = 2;

    @Test
    public void save_findById_delete_count() {
        Faq faq1 = new Faq();
        faq1.setFaqQuestion(question);
        faq1.setFaqAnswer(answer);
        faq1.setFaqCategory(category);
        faq1.setFaqBest(1);
        Faq faq2 = new Faq(null, question, answer, category, 0, null, null);

//        faqRepository.saveAndFlush(faq1);
//        faqRepository.saveAndFlush(faq2);

        // insert
        faqRepository.save(faq1);
        faqRepository.save(faq2);

        // 단건 조회
        Faq findFaq1 = faqRepository.findById(faq1.getFaqNo()).get();
        Faq findFaq2 = faqRepository.findById(faq2.getFaqNo()).get();
        assertThat(findFaq1).isEqualTo(faq1);
        assertThat(findFaq2).isEqualTo(faq2);

        // 삭제 검증
        long beforeCount = faqRepository.count();
        faqRepository.delete(faq1);
        faqRepository.delete(faq2);
        long deletedcount = faqRepository.count();
        assertThat(deletedcount).isEqualTo(beforeCount - insertNum);
    }

    @Test
    public void crud() {
        long beforeCount = faqRepository.count();

        // insert
        Faq faq1 = new Faq();
        faq1.setFaqQuestion(question);
        faq1.setFaqAnswer(answer);
        faq1.setFaqCategory(category);
        faq1.setFaqBest(1);
        Faq faq2 = new Faq(null, question, answer, category, 0, null, null);
        faqRepository.save(faq1);
        faqRepository.save(faq2);

        // 단건 조회
        Faq findFaq1 = faqRepository.findById(faq1.getFaqNo()).get();
        Faq findFaq2 = faqRepository.findById(faq2.getFaqNo()).get();
        assertThat(findFaq1).isEqualTo(faq1);
        assertThat(findFaq2).isEqualTo(faq2);

        // 수정 검증
        findFaq1.setFaqQuestion(update + " - " + question);
        findFaq1.setFaqAnswer(update + " - " + answer);
        findFaq1.setFaqCategory(update + " - " + category);
        findFaq1.setFaqBest(0);
        findFaq2.setFaqQuestion(update + " - " + question);
        findFaq2.setFaqAnswer(update + " - " + answer);
        findFaq2.setFaqCategory(update + " - " + category);
        findFaq2.setFaqBest(1);

        faqRepository.save(findFaq1);
        faqRepository.save(findFaq2);
//        faqRepository.flush();

        Faq updateFaq1 = faqRepository.findById(faq1.getFaqNo()).get();
        Faq updateFaq2 = faqRepository.findById(faq2.getFaqNo()).get();
        assertThat(updateFaq1.getFaqQuestion()).isEqualTo(findFaq1.getFaqQuestion());
        assertThat(updateFaq2.getFaqQuestion()).isEqualTo(findFaq2.getFaqQuestion());

        // 리스트 조회 검증
//        List<Faq> allFaq = faqRepository.findAll();
//        assertThat(allFaq.size()).isEqualTo((int)beforeCount + insertNum);

        // 삭제 검증
        faqRepository.delete(faq1);
        faqRepository.delete(faq2);
        long deletedcount = faqRepository.count();
        assertThat(deletedcount).isEqualTo(beforeCount);
    }

}
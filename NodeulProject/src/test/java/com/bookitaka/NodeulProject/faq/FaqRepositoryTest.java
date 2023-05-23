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

    @Test
    public void basicCRUD() {
        // insert
        Faq faq1 = new Faq();
        faq1.setFaqQuestion("question1");
        faq1.setFaqAnswer("answer1");
        faq1.setFaqCategory("category1");
        faq1.setFaqBest(1);
        Faq faq2 = new Faq(null, "question2", "answer2", "category2", 0, null, null);
        faqRepository.save(faq1);
        faqRepository.save(faq2);

        // 단건 조회
        Faq findFaq1 = faqRepository.findById(faq1.getFaqNo()).get();
        Faq findFaq2 = faqRepository.findById(faq2.getFaqNo()).get();
        assertThat(findFaq1).isEqualTo(faq1);
        assertThat(findFaq2).isEqualTo(faq2);

        // 리스트 조회 검증
        List<Faq> all = faqRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        // 삭제 검증
        faqRepository.delete(faq1);
        faqRepository.delete(faq2);
        long deletedcount = faqRepository.count();
        assertThat(deletedcount).isEqualTo(0);

    }


}
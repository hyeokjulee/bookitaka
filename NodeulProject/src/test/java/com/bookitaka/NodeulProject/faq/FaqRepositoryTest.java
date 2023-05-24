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
    FaqRepository repository;

    @Test
    public void saveTest() {
        // insert
        Faq faq1 = new Faq();
        faq1.setFaqQuestion("question1");
        faq1.setFaqAnswer("answer1");
        faq1.setFaqCategory("category1");
        faq1.setFaqBest(1);
        Faq faq2 = new Faq(null, "question2", "answer2", "category2", 0, null, null);
        repository.saveAndFlush(faq1);
        repository.saveAndFlush(faq2);

        // 단건 조회
        Faq findFaq1 = repository.findById(faq1.getFaqNo()).get();
        Faq findFaq2 = repository.findById(faq2.getFaqNo()).get();

        assertThat(findFaq1).isEqualTo(faq1);
        assertThat(findFaq2).isEqualTo(faq2);
    }
    @Test
    public void crud() {
        // insert
        Faq faq1 = new Faq();
        faq1.setFaqQuestion("question1");
        faq1.setFaqAnswer("answer1");
        faq1.setFaqCategory("category1");
        faq1.setFaqBest(1);
        Faq faq2 = new Faq(null, "question2", "answer2", "category2", 0, null, null);
        repository.save(faq1);
        repository.save(faq2);

        // 단건 조회
        Faq findFaq1 = repository.findById(faq1.getFaqNo()).get();
        Faq findFaq2 = repository.findById(faq2.getFaqNo()).get();
        assertThat(findFaq1).isEqualTo(faq1);
        assertThat(findFaq2).isEqualTo(faq2);

        // 수정 검증
        findFaq1.setFaqQuestion("update - question1");
        findFaq1.setFaqAnswer("update - answer1");
        findFaq1.setFaqCategory("update - category1");
        findFaq1.setFaqBest(0);
        findFaq2.setFaqQuestion("update - question2");
        findFaq2.setFaqAnswer("update - answer2");
        findFaq2.setFaqCategory("update - category2");
        findFaq2.setFaqBest(1);

//        repository.save(findFaq1);
//        repository.save(findFaq2);
        repository.flush();

        Faq updateFaq1 = repository.findById(faq1.getFaqNo()).get();
        Faq updateFaq2 = repository.findById(faq2.getFaqNo()).get();
        assertThat(updateFaq1.getFaqQuestion()).isEqualTo("update - question1");
        assertThat(updateFaq2.getFaqQuestion()).isEqualTo("update - question2");

        // 리스트 조회 검증
        List<Faq> all = repository.findAll();
        assertThat(all.size()).isEqualTo(2);

        // 삭제 검증
        repository.delete(faq1);
        repository.delete(faq2);
        long deletedcount = repository.count();
        assertThat(deletedcount).isEqualTo(0);

    }


}
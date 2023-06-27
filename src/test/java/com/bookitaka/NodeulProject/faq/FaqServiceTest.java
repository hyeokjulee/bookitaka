package com.bookitaka.NodeulProject.faq;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
//@Transactional
class FaqServiceTest {

    @Autowired
    FaqService service;

    @Test
    public void registerTest() {
        // register
        Faq faq1 = new Faq();
        faq1.setFaqQuestion("question1");
        faq1.setFaqAnswer("answer1");
        faq1.setFaqCategory("category1");
        faq1.setFaqBest(1);
        Faq faq2 = new Faq(null, "question2", "answer2", "category2", 0, null, null);
        service.registerFaq(faq1);
        service.registerFaq(faq2);

        // findOneFaq 단건 조회
        Faq findFaq1 = service.getOneFaq(faq1).get();
        Faq findFaq2 = service.getOneFaq(faq2).get();
        assertThat(findFaq1.getFaqQuestion()).isEqualTo(faq1.getFaqQuestion());
        assertThat(findFaq2.getFaqQuestion()).isEqualTo(faq2.getFaqQuestion());
    }

    @Test
    public void modifyTest() {
        // register
        Faq faq1 = new Faq();
        faq1.setFaqQuestion("question1");
        faq1.setFaqAnswer("answer1");
        faq1.setFaqCategory("category1");
        faq1.setFaqBest(1);
        Faq faq2 = new Faq(null, "question2", "answer2", "category2", 0, null, null);
        service.registerFaq(faq1);
        service.registerFaq(faq2);

        // findOneFaq 단건 조회
        Faq findFaq1 = service.getOneFaq(faq1).get();
        Faq findFaq2 = service.getOneFaq(faq2).get();
        assertThat(findFaq1.getFaqQuestion()).isEqualTo(faq1.getFaqQuestion());
        assertThat(findFaq2.getFaqQuestion()).isEqualTo(faq2.getFaqQuestion());

        // modifyFaq 수정 검증
        Faq newFaq1 = new Faq();
        newFaq1.setFaqQuestion("update - question1");
        newFaq1.setFaqAnswer("update - answer1");
        newFaq1.setFaqCategory("update - category1");
        newFaq1.setFaqBest(0);
        Faq newFaq2 = new Faq(null, "update - question2", "update - answer2",
                "update - category2", 1, null, null);

        service.modifyFaq(findFaq1.getFaqNo(), newFaq1);
        service.modifyFaq(findFaq2.getFaqNo(), newFaq2);

        System.out.println("Service - modify 호출 후: 번호" + findFaq1.getFaqNo() + "질문" + findFaq1.getFaqQuestion());

        Faq updateFaq1 = service.getOneFaq(findFaq1).get();
        Faq updateFaq2 = service.getOneFaq(findFaq2).get();
        System.out.println("Service - modify 후 DB에서 getOne: 번호" + updateFaq1.getFaqNo() + "질문" + updateFaq1.getFaqQuestion());

        assertThat(updateFaq1.getFaqQuestion()).isEqualTo(newFaq1.getFaqQuestion());
        assertThat(updateFaq2.getFaqQuestion()).isEqualTo(newFaq2.getFaqQuestion());
    }

    @Test
    public void basicCRUD() {


    }

    @Test
    public void removeTest() {
        // findAllFaq 리스트 조회 검증
        List<Faq> all = service.getAllFaq();
        assertThat(all.size()).isEqualTo(2);

        // removeFaq 삭제 검증
//        service.removeFaq(faq1);
//        service.removeFaq(faq2);
//        long deletedcount = service.countFaq();
//        assertThat(deletedcount).isEqualTo(0);
    }

    @Test
    public void apiKeyTest() {
        String keyword = "토지";
        String author = "박경리";
        service.isbnSend(keyword, author);
    }


}
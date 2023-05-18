package com.bookitaka.NodeulProject.faq;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FaqRepository extends JpaRepository<Faq, Long> {

    @Override
    List<Faq> findAll();

    @Override
    <S extends Faq> S save(S entity);

    @Override
    void delete(Faq entity);
}

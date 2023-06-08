package com.bookitaka.NodeulProject.sheet.mysheet;

import com.bookitaka.NodeulProject.member.model.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.function.Predicate;

public interface MysheetRepository extends JpaRepository<Mysheet, Long>, QuerydslPredicateExecutor<Mysheet> {

    public Mysheet findFirstBySheet_SheetFileuuidAndMemberOrderByMysheetEnddateDesc(String sheetFileUuid, Member member);

}

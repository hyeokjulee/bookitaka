package com.bookitaka.NodeulProject.mysheet;

import com.bookitaka.NodeulProject.cart.Cart;
import com.bookitaka.NodeulProject.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface MysheetRepository extends JpaRepository<Mysheet, Long>, QuerydslPredicateExecutor<Mysheet> {

    public Mysheet findFirstBySheet_SheetFileuuidAndMemberOrderByMysheetEnddateDesc(String sheetFileUuid, Member member);

    List<Mysheet> findByMember_MemberEmail(String memberEmail);
}

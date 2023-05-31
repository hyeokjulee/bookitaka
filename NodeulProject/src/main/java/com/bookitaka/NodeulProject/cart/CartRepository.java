package com.bookitaka.NodeulProject.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    List<Cart> findByMember_MemberEmailOrderByCartRegdate(String memberEmail);

    List<Cart> findByMember_MemberEmailAndSheet_SheetNo(String memberEmail, int sheetNo);
}
package com.bookitaka.NodeulProject.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    List<Cart> findByMemberEmailOrderByCartRegdate(String memberEmail);

    List<Cart> findByMemberEmailAndSheetNo(String memberEmail, int sheetNo);

    int countByMemberEmail(String memberEmail);
}
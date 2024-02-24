package com.bookitaka.NodeulProject.cart;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CartService {
    Cart addToCart(Cart cart);

    List<Cart> getCartByMemberEmail(String memberEmail);

    List<Cart> getCartByMemberEmailAndSheetNo(String memberEmail, int sheetNo);

    void deleteCartByMemberEmailAndSheetNo(String memberEmail, int sheetNo);

    void deleteCartsByMemberEmailAndSheetNos(String memberEmail, List<Integer> sheetNos);

    void deleteAllCartsByMemberEmail(String memberEmail);

    int getCountByMemberEmail(String memberEmail);
}
package com.bookitaka.NodeulProject.cart;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    @Override
    public Cart addToCart(Cart cart) {
        // 장바구니에 상품 추가 로직 구현
        return cartRepository.save(cart);
    }

    @Override
    public List<Cart> getCartByMemberEmail(String memberEmail) {
        // 회원의 장바구니 전체 조회 로직 구현
        return cartRepository.findByMemberEmailOrderByCartRegdate(memberEmail);
    }

    @Override
    public List<Cart> getCartByMemberEmailAndSheetNo(String memberEmail, int sheetNo) {
        // 회원의 장바구니 하나 조회 로직 구현
        return cartRepository.findByMemberEmailAndSheetNo(memberEmail, sheetNo);
    }

    @Override
    public void deleteAllCartsByMemberEmail(String memberEmail) {
        // 장바구니 비우기 로직 구현
        cartRepository.deleteAll(cartRepository.findByMemberEmailOrderByCartRegdate(memberEmail));
    }

    @Override
    public void deleteCartByMemberEmailAndSheetNo(String memberEmail, int sheetNo) {
        // 장바구니에서 상품 하나 삭제 로직 구현
        cartRepository.deleteAll(cartRepository.findByMemberEmailAndSheetNo(memberEmail, sheetNo));
    }

    @Override
    public void deleteCartsByMemberEmailAndSheetNos(String memberEmail, List<Integer> sheetNos) {
        // 장바구니에서 선택한 상품들 삭제 로직 구현
        for (Integer sheetNo:sheetNos) {
            cartRepository.deleteAll(cartRepository.findByMemberEmailAndSheetNo(memberEmail, sheetNo));
        }
    }
}
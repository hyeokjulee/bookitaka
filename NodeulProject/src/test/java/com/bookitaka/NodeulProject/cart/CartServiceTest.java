package com.bookitaka.NodeulProject.cart;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CartServiceTest {
    @Autowired
    CartService cartService;

    @Test
    void add() { // 장바구니에 추가
        List<Cart> selectCartList = cartService.getCartByMemberEmailAndSheetNo("user@example.com", 126);

        if (!selectCartList.isEmpty()){
            return;
        }

        //given
        Cart cart = new Cart();
        cart.setMemberEmail("user@example.com");
        cart.setSheetNo(126);

        //when
        cartService.addToCart(cart);

        //then
        Cart findCart = cartService.getCartByMemberEmail("user@example.com").get(0);
        assertThat(findCart.getMemberEmail()).isEqualTo(cart.getMemberEmail());
    }

    @Test
    void delete() { // 상품 하나 삭제
        //when
        cartService.deleteCartByMemberEmailAndSheetNo("user@example.com", 124);

        //then
        Cart findCart = cartService.getCartByMemberEmail("user@example.com").get(0);
        assertThat(findCart.getCartNo()).isEqualTo(4);
    }

    @Test
    void deleteAll() { // 장바구니 비우기
        //when
        cartService.deleteAllCartsByMemberEmail("user@example.com");

        //then
        assertThat(cartService.getCartByMemberEmail("user@example.com")).isEmpty();
    }

    @Test
    void deleteCarts() { // 선택한 상품들 삭제
        //when
        List<Integer> sheetNos = new ArrayList<>();
        sheetNos.add(125);
        sheetNos.add(126);
        cartService.deleteCartsByMemberEmailAndSheetNos("user@example.com", sheetNos);

        //then
        assertThat(cartService.getCartByMemberEmail("user@example.com")).isEmpty();
    }
}
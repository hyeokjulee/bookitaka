package com.bookitaka.NodeulProject.cart;

import org.junit.jupiter.api.AfterEach;
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

    private final String testingEmail = "user@example.com";
    private final int testingSheetNo = 1001;
    private final int n = 5;
    // DB 내에 member_email 이 "user@example.com" 인 member 가 존재해야하며
    // 1001 ~ 1005 의 sheet_no를 가진 sheet 들이 존재해야함

    @AfterEach
    void tearDown() {
        cartService.deleteAllCartsByMemberEmail(testingEmail);
    }

    @Test
    void addToCart_and_getCartByMemberEmailAndSheetNo() { // 담기
        //given
        Cart cart = new Cart();
        cart.setMemberEmail(testingEmail);
        cart.setSheetNo(testingSheetNo);

        //when
        cartService.addToCart(cart);

        //then
        Cart findCart = cartService.getCartByMemberEmailAndSheetNo(testingEmail, testingSheetNo).get(0);
        assertThat(findCart.getMemberEmail()).isEqualTo(cart.getMemberEmail());
    }

    @Test
    void deleteCartByMemberEmailAndSheetNo_and_getCartByMemberEmail() { // 하나 삭제
        //given
        Cart cart = new Cart();
        cart.setMemberEmail(testingEmail);
        cart.setSheetNo(testingSheetNo);

        //when
        cartService.addToCart(cart);

        //when
        cartService.deleteCartByMemberEmailAndSheetNo(testingEmail, testingSheetNo);

        //then
        List<Cart> cartList = cartService.getCartByMemberEmail(testingEmail);
        assertThat(cartList).isEmpty();
    }

    @Test
    void deleteCartsByMemberEmailAndSheetNos_and_getCartByMemberEmail() { // 선택 삭제
        for (int i = 0; i < n; i++) {
            //given
            Cart cart = new Cart();
            cart.setMemberEmail(testingEmail);
            cart.setSheetNo(testingSheetNo + i);

            //when
            cartService.addToCart(cart);
        }

        //when
        List<Integer> sheetNos = new ArrayList<>();
        sheetNos.add(testingSheetNo);
        sheetNos.add(testingSheetNo + 1);
        cartService.deleteCartsByMemberEmailAndSheetNos(testingEmail, sheetNos);

        //then
        assertThat(cartService.getCartByMemberEmail(testingEmail).get(0).getSheetNo()).isEqualTo(testingSheetNo + 2);
    }

    @Test
    void deleteAllCartsByMemberEmail_and_getCountByMemberEmail() { // 비우기
        for (int i = 0; i < 3; i++) {
            //given
            Cart cart = new Cart();
            cart.setMemberEmail(testingEmail);
            cart.setSheetNo(testingSheetNo + i);

            //when
            cartService.addToCart(cart);
        }

        //when
        cartService.deleteAllCartsByMemberEmail(testingEmail);

        //then
        assertThat(cartService.getCountByMemberEmail(testingEmail)).isEqualTo(0);
    }
}
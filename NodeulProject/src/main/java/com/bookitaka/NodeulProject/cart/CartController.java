package com.bookitaka.NodeulProject.cart;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final HttpServletRequest request;

    @GetMapping("/cart")
    public String cart() {
        return "cart/cart"; // 뷰 이름을 반환
    }

    @GetMapping("/getCarts")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getCarts() {
        Map<String, Object> response = new HashMap<>();
        String email = request.getRemoteUser();
        List<Cart> carts = cartService.getCartByMemberEmail("asd@asd.asd");
        response.put("success", true);
        response.put("carts", carts);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/deleteCart")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteCart(@RequestParam int sheetNo) {
        Map<String, Object> response = new HashMap<>();
        String email = request.getRemoteUser();
        cartService.deleteCartByMemberEmailAndSheetNo("asd@asd.asd", sheetNo);
        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/emptyCart")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteCart() {
        Map<String, Object> response = new HashMap<>();
        String email = request.getRemoteUser();
        cartService.deleteAllCartsByMemberEmail("asd@asd.asd");
        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/deleteSelectedCart")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteSelectedCart(@RequestBody List<Integer> selectedItems) {
        Map<String, Object> response = new HashMap<>();
        String email = request.getRemoteUser();
        cartService.deleteCartsByMemberEmailAndSheetNos("asd@asd.asd", selectedItems);
        response.put("success", true);
        return ResponseEntity.ok(response);
    }
}
package com.bookitaka.NodeulProject.cart;

import com.bookitaka.NodeulProject.member.service.MemberService;
import com.bookitaka.NodeulProject.sheet.Sheet;
import com.bookitaka.NodeulProject.sheet.SheetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MEMBER')")
@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final SheetService sheetService;
    private final HttpServletRequest request;

    @GetMapping("/cart") // 목록 페이지
    public String cart() {
        return "cart/cart"; // 뷰 이름을 반환
    }

    @GetMapping("/getCarts") // 목록 반환
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getCarts() {
        Map<String, Object> response = new HashMap<>();
        String email = request.getRemoteUser();
        List<Cart> carts = cartService.getCartByMemberEmail(email);
        List<Sheet> sheets = new ArrayList<>();;
        for (var i = 0; i < carts.size(); i++) {
            sheets.add(sheetService.getSheet(carts.get(i).getSheetNo()));
        }
        response.put("success", true);
        response.put("carts", carts);
        response.put("sheets", sheets);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/deleteCart") // 하나 삭제
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteCart(@RequestParam int sheetNo) {
        Map<String, Object> response = new HashMap<>();
        String email = request.getRemoteUser();
        cartService.deleteCartByMemberEmailAndSheetNo(email, sheetNo);
        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/emptyCart") // 전체 삭제
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteCart() {
        Map<String, Object> response = new HashMap<>();
        String email = request.getRemoteUser();
        cartService.deleteAllCartsByMemberEmail(email);
        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/deleteSelectedCart") // 선택 삭제
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteSelectedCart(@RequestBody List<Integer> selectedItems) {
        Map<String, Object> response = new HashMap<>();
        String email = request.getRemoteUser();
        cartService.deleteCartsByMemberEmailAndSheetNos(email, selectedItems);
        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/cartAdd") // 추가
    @ResponseBody
    public ResponseEntity<Map<String, Object>> cartAdd(@RequestParam int sheetNo) {
        Map<String, Object> response = new HashMap<>();
        String email = request.getRemoteUser();

        if(cartService.getCartByMemberEmailAndSheetNo(email, sheetNo).isEmpty()) {
            Cart cart = new Cart();
            cart.setMemberEmail(email);
            cart.setSheetNo(sheetNo);
            cartService.addToCart(cart);
            response.put("success", true);
        } else {
            response.put("fail", true);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/getCount") // 개수 반환
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getCount() {
        Map<String, Object> response = new HashMap<>();
        String email = request.getRemoteUser();
        int count = cartService.getCountByMemberEmail(email);
        response.put("success", true);
        response.put("count", count);
        return ResponseEntity.ok(response);
    }
}
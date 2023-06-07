package com.bookitaka.NodeulProject.payproc;

import com.bookitaka.NodeulProject.member.model.Member;
import com.bookitaka.NodeulProject.member.security.Token;
import com.bookitaka.NodeulProject.member.service.MemberService;
import com.bookitaka.NodeulProject.sheet.Sheet;
import com.bookitaka.NodeulProject.sheet.SheetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MEMBER')")
@Controller
@RequestMapping("/payproc")
@RequiredArgsConstructor
@Slf4j
public class PayprocController {
    private final MemberService memberService;
    private final SheetService sheetService;
    private final HttpServletRequest request;
    private final PayprocService payprocService;

    @GetMapping("/paying")
    public String paying(Model model, HttpServletRequest request) {
        model.addAttribute("member", memberService.whoami(request.getCookies(), Token.ACCESS_TOKEN));
        return "/payproc/paying";
    }


    @PostMapping("/paid")
    @ResponseBody
    public String requestAfterPay(@CookieValue("list") String list) {
        log.info("carts = {}", list);
        log.info("parsed carts = {}", parseCookie(list));
        return "hihi you successed";
    }

    private List<Integer> parseCookie(String input) {
        List<Integer> numberList = new ArrayList<>();

        // 대괄호와 쌍따옴표를 제거한 후 숫자 문자열 추출
        String numbersString = input.replace("[", "").replace("]", "").replaceAll("\"", "");

        String[] numberStrings = numbersString.split(",");
        for (String numberString : numberStrings) {
            int number = Integer.parseInt(numberString.trim());
            numberList.add(number);
        }

        return numberList;
    }

    @GetMapping("/complete")
    public String payCompletePage() {
        return "payComplete";
    }


    @PostMapping("/getSheets")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getSheets(@RequestBody List<Integer> sheetNos) {
        Map<String, Object> response = new HashMap<>();
        List<Sheet> sheets = new ArrayList<>();;
        for (var i = 0; i < sheetNos.size(); i++) {
            sheets.add(sheetService.getSheet(sheetNos.get(i)));
        }
        response.put("success", true);
        response.put("sheets", sheets);

        // 내 정보
        String email = request.getRemoteUser();
        Member member = memberService.search(email);
        response.put("member", member);

        return ResponseEntity.ok(response);
    }
}

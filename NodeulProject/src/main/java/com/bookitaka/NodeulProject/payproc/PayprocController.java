package com.bookitaka.NodeulProject.payproc;

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

    @GetMapping("/paying")
    public String paying(Model model, HttpServletRequest request) {
        model.addAttribute("member", memberService.whoami(request.getCookies(), Token.ACCESS_TOKEN));
        return "/payproc/paying";
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
        return ResponseEntity.ok(response);
    }
}

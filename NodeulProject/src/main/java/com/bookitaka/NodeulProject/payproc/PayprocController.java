package com.bookitaka.NodeulProject.payproc;

import com.bookitaka.NodeulProject.coupon.CouponService;
import com.bookitaka.NodeulProject.member.model.Member;
import com.bookitaka.NodeulProject.member.security.Token;
import com.bookitaka.NodeulProject.member.service.MemberService;
import com.bookitaka.NodeulProject.sheet.Sheet;
import com.bookitaka.NodeulProject.sheet.SheetService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

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
    private final CouponService couponService;
    private final HttpServletRequest request;
    private final PayprocService payprocService;

    private final WebClient webClient;

    @Value("${portone-rest-api-key}")
    private String apiKey;

    @Value("${portone-rest-api-secret}")
    private String apiSecret;


    @GetMapping("/paying")
    public String paying(Model model, HttpServletRequest request) {

        Member member = memberService.whoami(request.getCookies(), Token.ACCESS_TOKEN);
        model.addAttribute("couponCnt", couponService.getValidCouponCntByMemberEmail(member.getMemberEmail()));
        model.addAttribute("member", member);
        return "/payproc/paying";
    }

    @GetMapping("/complete")
    public String payCompletePage(@CookieValue("carts") String carts,
                                  Model model) {
        List<Long> sheetNumListInCart = parseCookie(carts);
        List<Sheet> sheetList = new ArrayList<>();

        for (Long sheetNo: sheetNumListInCart) {
            sheetList.add(sheetService.getSheet(Math.toIntExact(sheetNo)));
        }

        model.addAttribute("sheetList", sheetList);


        return "/payproc/payComplete";
    }


    @PostMapping("/verifyBefore")
    @ResponseBody
    public int verifyBefore(@RequestBody VeriBeforeDto veriBeforeDto) throws JsonProcessingException {
        String url = "https://api.iamport.kr/payments/prepare";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        log.info("VeriBefore = {}", veriBeforeDto);

        // 액세스 토큰(access token) 발급 받기
        String accessToken = getAccessToken();
        headers.setBearerAuth(accessToken);

        ResponseEntity<String> response = webClient.post()
                .uri(url)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .bodyValue(veriBeforeDto)
                .retrieve()
                .toEntity(String.class)
                .block();

        if (response != null) {
            int statusCode = response.getStatusCodeValue();
            String responseBody = response.getBody();
            HttpHeaders responseHeaders = response.getHeaders();

            log.info("Status Code: = {}", statusCode);
            log.info("Response Body: = {}", responseBody);
            log.info("Response Headers: = {}", responseHeaders);

            return statusCode ;
        }

        return 400;
    }


    @PostMapping("/paid")
    @ResponseBody
    public ResponseEntity<String> verifyAndRequestAfterPay(@RequestBody PayMakeDto payMakeDto, @CookieValue("carts") String carts) {

        VeriAfterDto veriAfterDto = new VeriAfterDto(payMakeDto.getImpId(), Math.toIntExact(payMakeDto.getPaymentPrice()));

        log.info("verifyAfterDto = {}", veriAfterDto);
        if (!verifyAfter(veriAfterDto)) {
            // 예외 발생 시
            cancelPayWhenFailAfterVeri(veriAfterDto, "사후 검증 오류");
            return ResponseEntity.badRequest().body("사후 검증 오류");
        }

        List<Long> sheetNumListInCart = parseCookie(carts);

        log.info("parsed carts = {}", sheetNumListInCart);

        payMakeDto.setSheetNoList(sheetNumListInCart);
        log.info("payMakeDto", payMakeDto);

        payprocService.makePay(payMakeDto);

        return ResponseEntity.ok().body("결제 완료");
    }

    @PostMapping("/couponPaid")
    @ResponseBody
    public ResponseEntity<String> verifyAndRequestAfterCouponPay(@RequestBody PayMakeDto payMakeDto) {
        VeriAfterDto veriAfterDto = new VeriAfterDto(payMakeDto.getImpId(), Math.toIntExact(payMakeDto.getPaymentPrice()));

        log.info("verifyAfterDto = {}", veriAfterDto);

        if (!verifyAfter(veriAfterDto)) {
            // 예외 발생 시
            cancelPayWhenFailAfterVeri(veriAfterDto, "사후 검증 오류");
            return ResponseEntity.badRequest().body("사후 검증 오류");
        }

        log.info("payMakeDto", payMakeDto);

        try {
            payprocService.makeCouponPay(payMakeDto);
        } catch (Exception e) {
            cancelPayWhenFailAfterVeri(veriAfterDto, "사후 검증 오류");
            return ResponseEntity.badRequest().body("사후 검증 오류");
        }

        return ResponseEntity.ok().body("결제 완료");
    }

    public boolean verifyAfter(VeriAfterDto veriAfterDto) {
        try {
            // req의 body에서 imp_uid, merchant_uid 추출
            String imp_uid = veriAfterDto.getImpUid();

            // 액세스 토큰(access token) 발급 받기
            String accessToken = getAccessToken();

            log.info("verifyAfter dto = {}", veriAfterDto);
            // imp_uid로 포트원 서버에서 결제 정보 조회
            String paymentData = getPaymentData(imp_uid, accessToken);

            // ObjectMapper 인스턴스 생성
            ObjectMapper objectMapper = new ObjectMapper();

            // JSON 문자열 파싱
            JsonNode jsonNode = objectMapper.readTree(paymentData);

            // "amount" 필드 값 price로 가져오기
            int price = jsonNode.get("response").get("amount").asInt();


            if (veriAfterDto.getAmount() == price) {
                return true;
            }
            else {
                return false;
            }
        } catch (Exception e) {
            log.error("사후검증 중 문제발생", e);
            return false;
        }
    }

    public boolean cancelPayWhenFailAfterVeri(VeriAfterDto veriAfterDto, String reason) {
        try {
            /* 액세스 토큰(access token) 발급 */
            String accessToken = getAccessToken();

            String url = "https://api.iamport.kr/payments/cancel";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessToken);

            String response = webClient.post()
                    .uri(url)
                    .headers(httpHeaders -> httpHeaders.addAll(headers))
                    .bodyValue("{\"reason\":\"" + reason + "\",\"imp_uid\":\"" + veriAfterDto.getImpUid() + "\",\"checksum\":" + veriAfterDto.getAmount() + "}")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            log.info("response cancelPay = {}", response);
            // ObjectMapper 인스턴스 생성
            ObjectMapper objectMapper = new ObjectMapper();

            // JSON 문자열 파싱
            JsonNode jsonNode = objectMapper.readTree(response);

            // "code" 필드 값 가져오기
            int code = jsonNode.get("code").asInt();

            if (code == 0) {
                log.info("환불 성공, code = {}", code);
                return true;
            } else {
                log.info("환불 실패, code = {}", code);
                return false;
            }

        } catch (Exception e) {
            return false;
        }
    }

    private String getAccessToken() throws JsonProcessingException {
        String url = "https://api.iamport.kr/users/getToken";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String response = webClient.post()
                .uri(url)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .bodyValue("{\"imp_key\":\"" + apiKey + "\",\"imp_secret\":\"" + apiSecret + "\"}")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        log.info("response accessToken = {}", response);
        // ObjectMapper 인스턴스 생성
        ObjectMapper objectMapper = new ObjectMapper();

        // JSON 문자열 파싱
        JsonNode jsonNode = objectMapper.readTree(response);

        // "access_token" 필드 값 가져오기
        String accessToken = jsonNode.get("response").get("access_token").asText();

        return accessToken;
    }
    private String getPaymentData(String imp_uid, String accessToken) throws JsonProcessingException {
        String url = "https://api.iamport.kr/payments/";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        String response = webClient.post()
                .uri(url + imp_uid)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        log.info("getPaymentData response = {}", response);

        if (response != null) {
            return response;
        } else {
            throw new RuntimeException("Failed to get payment data");
        }
    }





    private List<Long> parseCookie(String input) {
        List<Long> numberList = new ArrayList<>();

        // 대괄호와 쌍따옴표를 제거한 후 숫자 문자열 추출
        String numbersString = input.replace("[", "").replace("]", "").replaceAll("\"", "");

        String[] numberStrings = numbersString.split(",");
        for (String numberString : numberStrings) {
            Long number = Long.valueOf(numberString.trim());
            numberList.add(number);
        }

        return numberList;
    }


    @PostMapping("/getSheets")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getSheets(@RequestBody List<Integer> sheetNos) {
        Map<String, Object> response = new HashMap<>();
        List<Sheet> sheets = new ArrayList<>();
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

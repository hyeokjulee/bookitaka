package com.bookitaka.NodeulProject.payproc;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/verifyIamport")
public class VerifyController {

    @Value("${portone-rest-api-key}")
    private String apiKey;

    @Value("${portone-rest-api-secret}")
    private String apiSecret;

    /** Iamport 결제 검증 컨트롤러 **/
    private IamportClient iamportClient;

    /** 프론트에서 받은 PG사 결괏값을 통해 아임포트 토큰 발행 **/
    @PostMapping("/{imp_uid}")
    public IamportResponse<Payment> paymentByImpUid(@PathVariable("imp_uid") String imp_uid) throws IamportResponseException, IOException {
        log.info("paymentByImpUid 진입 = {}", imp_uid);
        iamportClient = new IamportClient(apiKey, apiSecret);

        return iamportClient.paymentByImpUid(imp_uid);
    }
}
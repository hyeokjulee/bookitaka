package com.bookitaka.NodeulProject.payproc;

import com.bookitaka.NodeulProject.member.model.Member;
import lombok.Data;

import java.util.List;

@Data
public class PayMakeDto {

    private String paymentUuid;

    private String paymentInfo;

    private Long paymentPrice;

    private String memberEmail;

    private String receiptUrl;

    private String payMethod;

    private List<Long> sheetNoList;

    private String impId;

    private String mySheetMeans;

}

package com.bookitaka.NodeulProject.payproc;

import com.bookitaka.NodeulProject.member.model.Member;
import lombok.Data;

import java.util.List;

@Data
public class PayMakeDto {

    private String paymentUuid;

    private String paymentInfo;

    private Long paymentPrice;

    private List<Long> sheetNoList;

    private String sheetMeans;

}

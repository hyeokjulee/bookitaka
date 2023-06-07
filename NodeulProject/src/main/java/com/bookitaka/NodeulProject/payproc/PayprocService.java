package com.bookitaka.NodeulProject.payproc;

import com.bookitaka.NodeulProject.member.model.Member;

public interface PayprocService {

    boolean makePay(PayMakeDto payMakeDto, Member member);

}

package com.bookitaka.NodeulProject.faq;

import java.util.List;

public interface FaqCategory {


    String faqCategoryArr[] = {
            "전체",
            "BEST",
            "회원",
            "활동지",
            "결제",
            "기타",
            "추가"
    };

    List<String> faqAllCategory = List.of(faqCategoryArr);

}

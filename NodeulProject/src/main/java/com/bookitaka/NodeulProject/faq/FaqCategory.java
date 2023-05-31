package com.bookitaka.NodeulProject.faq;

import java.util.List;

public interface FaqCategory {


    String faqCategoryArr[] = {
            "회원",
            "활동지",
            "결제",
            "기타"
    };

    List<String> faqAllCategory = List.of(faqCategoryArr);

}

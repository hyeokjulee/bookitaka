package com.bookitaka.NodeulProject.sheet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Controller
@RequestMapping(value = "/sheet")
@RequiredArgsConstructor
public class SheetController {

    @Value("${isbn-api-key}")
    private String isbn_api_key;

    @GetMapping("/add")
    public String add() {
        return "/sheet/sheetAddForm";
    }
    @GetMapping("/bookSearchForm")
    public String bookSearchForm() {
        return "/sheet/bookSearch";
    }

    @PostMapping("/booksearch")
    public void search(@RequestParam String keyword, @RequestParam String author, HttpServletResponse response) {
        String url = "https://www.nl.go.kr/NL/search/openApi/search.do?key=" + isbn_api_key
                + "&kwd=" + keyword
                + "&detailSearch=true&f1=title&v1=" + keyword
                + "&f2=author&v2=" + author
                + "&pageSize=5";

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);

        response.setContentType("text/xml");
        response.setCharacterEncoding("UTF-8");
        try {
            response.getWriter().write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

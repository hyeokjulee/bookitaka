package com.bookitaka.NodeulProject.request;

import com.bookitaka.NodeulProject.sheet.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;

    @Value("${isbn-api-key}")
    private String isbn_api_key;


    @Override
    public void registerRequest(Request request) {
        requestRepository.save(request);
    }

    @Override
    public Optional<Request> getOneRequest(Long RequestNo) {
        return Optional.empty();
    }

    @Override
    public void modifyRequest(Request requestModified) {

    }

    @Override
    public void removeRequest(Request request) {

    }

    @Override
    public Page<Request> getAllRequestByRequestIsdone(int requestIsdone, Pageable pageable) {
        return requestRepository.findAllByRequestIsdoneOrderByRequestRegdateDesc(requestIsdone, pageable);
    }

    @Override
    public Map<String, Object> searchBook(String keyword, String authorSearch, Integer pageNum) {
        int pageSize = 5;   // 검색 결과 페이지당 출력할 개수
        String url = "https://www.nl.go.kr/NL/search/openApi/search.do?" +
                "key=" + isbn_api_key +
                "&kwd=" + keyword +
                "&detailSearch=true" +
                "&f1=title" +
                "&v1=" + keyword +
                "&f2=author" +
                "&v2=" + authorSearch +
                "&pageNum=" + pageNum +
                "&pageSize=" + pageSize;

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);
//        log.info("Service - getForObject = {}", result.toString());

        String currentPageNum = "";
        String total = "";

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(result)));

            Element rootElement = document.getDocumentElement();
            NodeList itemNodeList = rootElement.getElementsByTagName("item");
            NodeList paramDataNodeList = rootElement.getElementsByTagName("paramData");
            Node paramDataNode = paramDataNodeList.item(0);

            if (paramDataNode.getNodeType() == Node.ELEMENT_NODE) {
                Element paramDataElement = (Element) paramDataNode;
                currentPageNum = paramDataElement.getElementsByTagName("pageNum").item(0).getTextContent();
                total = paramDataElement.getElementsByTagName("total").item(0).getTextContent();
            }

            List<BookDto> bookList = new ArrayList<>();
            for (int i = 0; i < itemNodeList.getLength(); i++) {
                Node itemNode = itemNodeList.item(i);
                if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element itemElement = (Element) itemNode;
                    String title = itemElement.getElementsByTagName("title_info").item(0).getTextContent();
                    String author = itemElement.getElementsByTagName("author_info").item(0).getTextContent();
                    String pub = itemElement.getElementsByTagName("pub_info").item(0).getTextContent();
                    String isbn = itemElement.getElementsByTagName("isbn").item(0).getTextContent();
                    String img = itemElement.getElementsByTagName("image_url").item(0).getTextContent();

                    BookDto bookDto = new BookDto();
                    bookDto.setSheetBooktitle(title);
                    bookDto.setSheetBookauthor(author);
                    bookDto.setSheetBookpublisher(pub);
                    bookDto.setSheetBookisbn(isbn);
                    bookDto.setSheetBookimgname(img);
                    bookList.add(bookDto);
                }
            }

            int totalPageNum = Integer.parseInt(total) == 1 ? 0 : (Integer.parseInt(total) / pageSize) + 1;
            log.info("bookList = {}", bookList);
            log.info("total = {}", total);
//            log.info("totalPageNum = {}", totalPageNum);
            log.info("currentPageNum = {}", currentPageNum);

            Map<String, Object> searchResult = new HashMap<>();
            searchResult.put("bookList", bookList);
            searchResult.put("total", total);
            searchResult.put("totalPageNum", totalPageNum);
            searchResult.put("currentPageNum", currentPageNum);

            return searchResult;

        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }

//        return result;
    }

}

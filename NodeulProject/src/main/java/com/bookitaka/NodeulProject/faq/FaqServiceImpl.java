package com.bookitaka.NodeulProject.faq;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FaqServiceImpl implements FaqService{

    @Value("${isbn-api-key}")
    private String isbn_api_key;


    private final FaqRepository faqRepository;

    @Override
    public void registerFaq(Faq faq) {
        faqRepository.save(faq);
    }

    @Override
    public List<Faq> getAllFaq() {
        return faqRepository.findAll();
    }

    @Override
    public Optional<Faq> getOneFaq(Long faqNo) {
        return faqRepository.findById(faqNo);
    }

    @Override
    @Modifying(clearAutomatically = true)
    public void modifyFaq(Long faqNo, Faq newFaq) {
        Faq faqFound = faqRepository.findById(faqNo).get();
        System.out.println("modifyFaq - 번호" + faqFound.getFaqNo() + "질문" + faqFound.getFaqQuestion());
        faqFound.setFaqQuestion(newFaq.getFaqQuestion());
        faqFound.setFaqAnswer(newFaq.getFaqAnswer());
        faqFound.setFaqCategory(newFaq.getFaqCategory());
        faqFound.setFaqBest(newFaq.getFaqBest());
        faqRepository.save(faqFound);
        System.out.println("modifyFaq - 번호" + faqFound.getFaqNo() + "질문" + faqFound.getFaqQuestion());
    }

    @Override
    public void removeFaq(Faq faq) {
        faqRepository.delete(faq);
    }

    @Override
    public long countFaq() {
        return faqRepository.count();
    }

    @Override
    public List<String> getAllFaqCategory() {
        return FaqCategory.faqAllCategory;
    }

    @Override
    public List<Faq> getAllFaqByFaqCategory(String faqCategory) {
        return faqRepository.findAllByFaqCategory(faqCategory);
    }

    @Override
    public List<Faq> getAllFaqByFaqBest() {
        return faqRepository.findAllByFaqBest(1);
    }


    // tag값의 정보를 가져오는 메소드
    private static String getTagValue(String tag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
        Node nValue = (Node) nlList.item(0);
        if(nValue == null)
            return null;
        return nValue.getNodeValue();
    }

    @Override
    public void isbnSend(String keyword, String author) {
        try {
            String url = "https://www.nl.go.kr/NL/search/openApi/search.do?" +
                    "key=" + isbn_api_key +
                    "&kwd=" + URLEncoder.encode(keyword, "UTF-8") +
                    "&detailSearch=true" +
                    "&f1=title" +
                    "&v1=" + URLEncoder.encode(keyword, "UTF-8") +
                    "&f2=author" +
                    "&v2=" + URLEncoder.encode(author, "UTF-8") +
                    "&pageSize=5";

            DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactoty.newDocumentBuilder();
            Document doc = dBuilder.parse(url);


        // root tag
        doc.getDocumentElement().normalize();
        System.out.println("Root element: " + doc.getDocumentElement().getNodeName()); // Root element: result

        // 파싱할 tag
        NodeList nList = doc.getElementsByTagName("item");
        System.out.println("파싱할 리스트 수 : "+ nList.getLength());  // 파싱할 리스트 수 :  5

        for(int temp = 0; temp < nList.getLength(); temp++){
            Node nNode = nList.item(temp);
            if(nNode.getNodeType() == Node.ELEMENT_NODE){

                Element eElement = (Element) nNode;
                System.out.println("######################");
                //System.out.println(eElement.getTextContent());
                System.out.println("제목  : " + getTagValue("title_info", eElement));
                System.out.println("작가  : " + getTagValue("author_info", eElement));
                System.out.println("출판사 : " + getTagValue("pub_info", eElement));
                System.out.println("표지 이미지  : " + getTagValue("image_url", eElement));
                System.out.println("ISBN  : " + getTagValue("isbn", eElement));
            }	// for end
        }	// if end

        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }
}

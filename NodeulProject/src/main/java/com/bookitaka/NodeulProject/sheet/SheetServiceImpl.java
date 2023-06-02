package com.bookitaka.NodeulProject.sheet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
public class SheetServiceImpl implements SheetService{

    private final SheetRepository sheetRepository;
    private final GenreRepository genreRepository;
    private final AgeGroupRepository ageGroupRepository;


    @Value("${file.bookImg.dir}")
    private String bookImgDir;

    @Value("${file.sheetFile.dir}")
    private String sheetFileDir;

    @Value("${isbn-api-key}")
    private String isbn_api_key;

    @Override
    public Sheet registerSheet(SheetRegDto sheetRegDto, UploadFile uploadBookImg, UploadFile uploadSheetFile) {
        Sheet sheet = new Sheet();
        log.info("sheetRegDto = {}", sheetRegDto);
        sheet.setSheetBooktitle(sheetRegDto.getSheetBooktitle());
        sheet.setSheetBookauthor(sheetRegDto.getSheetBookauthor());
        sheet.setSheetBookpublisher(sheetRegDto.getSheetBookpublisher());
        sheet.setSheetBookisbn(sheetRegDto.getSheetBookisbn());
        sheet.setSheetPrice(sheetRegDto.getSheetPrice());

        sheet.setSheetBookimguuid(uploadBookImg.uuid);
        sheet.setSheetBookimgname(uploadBookImg.fileName);

        sheet.setSheetFileuuid(uploadSheetFile.uuid);
        sheet.setSheetFilename(uploadSheetFile.fileName);

        sheet.setSheetGenre(genreRepository.findBySheetGenreName(sheetRegDto.getSheetGenreName()));
        sheet.setSheetAgegroup(ageGroupRepository.findBySheetAgegroupName(sheetRegDto.getSheetAgegroupName()));
        sheet.setSheetContent(sheetRegDto.getSheetContent());

        return sheetRepository.createSheet(sheet);
    }

    @Override
    public UploadFile storeBookImg(MultipartFile sheetBookImg) throws IOException {
        String bookImgName = sheetBookImg.getOriginalFilename();

        String uuid = UUID.randomUUID().toString();
        String bookImgFullPath = bookImgDir + uuid + bookImgName;
        log.info("bookImg 저장 fullPath={}", bookImgFullPath);
        sheetBookImg.transferTo(new File(bookImgFullPath));

        //테스트 데이터 넣기용
//        Files.copy(sheetBookImg.getInputStream(), Paths.get(bookImgFullPath), StandardCopyOption.REPLACE_EXISTING);

        return new UploadFile(uuid, bookImgName);
    }

    @Override
    public UploadFile storeSheetFile(MultipartFile sheetFile) throws IOException {

        String sheetFileName = sheetFile.getOriginalFilename();

        String uuid = UUID.randomUUID().toString();
        String sheetFileFullPath = sheetFileDir + uuid + sheetFileName;
        log.info("sheetFile 저장 fullPath = {}", sheetFileFullPath);
        sheetFile.transferTo(new File(sheetFileFullPath));
        //테스트 데이터 넣기용
//        Files.copy(sheetFile.getInputStream(), Paths.get(sheetFileFullPath), StandardCopyOption.REPLACE_EXISTING);

        return new UploadFile(uuid, sheetFileName);
    }

    @Override
    public Sheet getSheet(int sheetNo) {
        return sheetRepository.findSheetByNo(sheetNo).orElse(null);
    }

    @Override
    public Long getSheetCnt(String genre, String ageGroup, String searchType, String searchWord) {
        log.info("service cnt genre = {}", genre);
        log.info("service cnt ageGroup = {}", ageGroup);

        if (!genre.equals("")) {
            return sheetRepository.countSheetByGenre(genre, searchType, searchWord);
        } else if (!ageGroup.equals("")) {
            return sheetRepository.countSheetByAgeGroup(ageGroup, searchType, searchWord);
        } else {
            return sheetRepository.countSheet(searchType, searchWord);
        }

    }

    @Override
    public List<Sheet> getAllSheets(String genre, String ageGroup, SheetCri cri) {
        log.info("service sheet genre = {}", genre);
        log.info("service sheet ageGroup = {}", ageGroup);

        if (!genre.equals("")) {
            return sheetRepository.findAllSheetByGenre(genre, cri);
        } else if (!ageGroup.equals("")) {
            return sheetRepository.findAllSheetByAgeGroup(ageGroup, cri);
        } else {
            return sheetRepository.findAllSheet(cri);
        }

    }


    @Override
    public boolean modifySheet(int sheetNo, SheetUpdateDto sheetUpdateDto) {
        return sheetRepository.updateSheet(sheetNo, sheetUpdateDto);
    }

    @Override
    public boolean removeSheet(int sheetNo) {
        Sheet sheet = sheetRepository.findSheetByNo(sheetNo).orElse(null);
        boolean result1 = removeStoredFile(bookImgDir + sheet.getSheetBookimguuid() + sheet.getSheetBookimgname());
        boolean result2 = removeStoredFile(sheetFileDir + sheet.getSheetFileuuid() + sheet.getSheetFilename());

        if (result1 && result2) {
            return sheetRepository.deleteSheet(sheetNo);
        }
        return false;

    }

    @Override
    public boolean removeStoredFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
            return true;
        }
        return false;
    }

    @Override
    public List<SheetGenre> getAllSheetGenre() {
        return genreRepository.findAll();
    }

    @Override
    public List<SheetAgegroup> getAllSheetAgeGroup() {
        return ageGroupRepository.findAll();
    }

    @Override
    public String getFileNameByUuid(String uuid) {
        return sheetRepository.findFileNameByUuid(uuid);
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
//                    String img = itemElement.getElementsByTagName("image_url").item(0).getTextContent();

                    BookDto bookDto = new BookDto();
                    bookDto.setSheetBooktitle(title);
                    bookDto.setSheetBookauthor(author);
                    bookDto.setSheetBookpublisher(pub);
                    bookDto.setSheetBookisbn(isbn);
//                    bookDto.setSheetBookimgname(img);
                    bookList.add(bookDto);
                }
            }

            int totalPageNum = Integer.parseInt(total) == 1 ? 0 : (Integer.parseInt(total) / pageSize) + 1;
            log.info("bookList = {}", bookList);
            log.info("total = {}", total);
            log.info("totalPageNum = {}", totalPageNum);
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

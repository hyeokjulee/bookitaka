package com.bookitaka.NodeulProject.sheet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

    @Value("${file.preview.dir}")
    private String previewDir;


//    @Value("${isbn-api-key}")
//    private String isbn_api_key;

    @Override
    public Sheet registerSheet(SheetRegDto sheetRegDto, UploadFile uploadBookImg, UploadFile uploadSheetFile) {
        Sheet sheet = new Sheet();
        log.info("sheetRegDto = {}", sheetRegDto);
        sheet.setSheetBooktitle(sheetRegDto.getSheetBooktitle());
        sheet.setSheetBookauthor(sheetRegDto.getSheetBookauthor());
        sheet.setSheetBookpublisher(sheetRegDto.getSheetBookpublisher());
        sheet.setSheetBookisbn(sheetRegDto.getSheetBookisbn());
        sheet.setSheetPrice(sheetRegDto.getSheetPrice());

        if (uploadBookImg != null) {
            sheet.setSheetBookimguuid(uploadBookImg.uuid);
            sheet.setSheetBookimgname(uploadBookImg.fileName);
        }

        if (uploadSheetFile != null) {
            sheet.setSheetFileuuid(uploadSheetFile.uuid);
            sheet.setSheetFilename(uploadSheetFile.fileName);
        }


        log.info("-----------------------");
        sheet.setSheetGenre(genreRepository.findTopBySheetGenreName(sheetRegDto.getSheetGenreName()));
        sheet.setSheetAgegroup(ageGroupRepository.findTopBySheetAgegroupName(sheetRegDto.getSheetAgegroupName()));
        sheet.setSheetContent(sheetRegDto.getSheetContent());
        log.info("----------------------");

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


        //미리보기 따로 저장을 위한 코드 시작
        // PDF 파일을 PDDocument로 로드
        InputStream is = sheetFile.getInputStream();
        PDDocument document = PDDocument.load(is);
        int numberOfPages = document.getNumberOfPages();

        if (numberOfPages < 3) {
            log.info("pdf가 3장이 안돼서 미리보기 생성 불가. 장수 = {}", numberOfPages);
        } else {

            // 저장할 페이지 수
            int numPagesToSave = 3;

            // 앞부분만 따로 저장할 새로운 PDDocument 생성
            PDDocument newDocument = new PDDocument();
            for (int i = 0; i < numPagesToSave; i++) {
                // 원본 문서에서 페이지 가져오기
                PDPage page = document.getPage(i);

                // 새로운 문서에 페이지 추가
                newDocument.addPage(page);
            }


            // 새로운 문서 저장
            newDocument.save(previewDir + uuid + sheetFileName);

            // 문서 닫기
            newDocument.close();
        }
        document.close();
        //미리보기 따로 저장을 위한 코드 끝
        is.close();

        sheetFile.transferTo(new File(sheetFileFullPath));
        //테스트 데이터 넣기용
//        Files.copy(sheetFile.getInputStream(), Paths.get(sheetFileFullPath), StandardCopyOption.REPLACE_EXISTING);

        return new UploadFile(uuid, sheetFileName);
    }

    @Override
    public Sheet getSheet(int sheetNo) {
        Sheet sheet = sheetRepository.findSheetByNo(sheetNo).orElse(null);
        if (sheet != null) {
            sheetRepository.plusOneSheetHit(sheetNo);
            return sheet;
        }
        return null;
    }

    @Override
    public Long getSheetCnt(String genre, String ageGroup, String searchType, String searchWord) {
        log.info("service cnt genre = {}", genre);
        log.info("service cnt ageGroup = {}", ageGroup);

        if (!genre.isEmpty()) {
            return sheetRepository.countSheetByGenre(genre, searchType, searchWord);
        } else if (!ageGroup.isEmpty()) {
            return sheetRepository.countSheetByAgeGroup(ageGroup, searchType, searchWord);
        } else {
            return sheetRepository.countSheet(searchType, searchWord);
        }

    }

    @Override
    public List<Sheet> getAllSheets(String genre, String ageGroup, SheetCri cri) {
        log.info("service sheet genre = {}", genre);
        log.info("service sheet ageGroup = {}", ageGroup);

        if (!genre.isEmpty()) {
            return sheetRepository.findAllSheetByGenre(genre, cri);
        } else if (!ageGroup.isEmpty()) {
            return sheetRepository.findAllSheetByAgeGroup(ageGroup, cri);
        } else {
            return sheetRepository.findAllSheet(cri);
        }

    }


    @Override
    public boolean modifySheet(int sheetNo, SheetUpdateDto sheetUpdateDto) {
        Sheet sheet = sheetRepository.findSheetByNo(sheetNo).orElse(null);
        if (sheetUpdateDto.getSheetBookimgname() == null) {
            sheetUpdateDto.setSheetBookimguuid(sheet.getSheetBookimguuid());
            sheetUpdateDto.setSheetBookimgname(sheet.getSheetBookimgname());
        }
        if (sheetUpdateDto.getSheetFilename() == null) {
            sheetUpdateDto.setSheetFileuuid(sheet.getSheetFileuuid());
            sheetUpdateDto.setSheetFilename(sheet.getSheetFilename());
        }
        log.info("sheetUpdataDto = {}", sheetUpdateDto);

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



//    @Override
//    public Map<String, Object> searchBook(String keyword, String authorSearch, Integer pageNum) {
//        int pageSize = 5;   // 검색 결과 페이지당 출력할 개수
//        String url = "https://www.nl.go.kr/NL/search/openApi/search.do?" +
//                "key=" + isbn_api_key +
//                "&kwd=" + keyword +
//                "&detailSearch=true" +
//                "&f1=title" +
//                "&v1=" + keyword +
//                "&f2=author" +
//                "&v2=" + authorSearch +
//                "&pageNum=" + pageNum +
//                "&pageSize=" + pageSize;
//
//        RestTemplate restTemplate = new RestTemplate();
//        String result = restTemplate.getForObject(url, String.class);
////        log.info("Service - getForObject = {}", result.toString());
//
//        String currentPageNum = "";
//        String total = "";
//
//        try {
//            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder builder = factory.newDocumentBuilder();
//            Document document = builder.parse(new InputSource(new StringReader(result)));
//
//            Element rootElement = document.getDocumentElement();
//            NodeList itemNodeList = rootElement.getElementsByTagName("item");
//            NodeList paramDataNodeList = rootElement.getElementsByTagName("paramData");
//            Node paramDataNode = paramDataNodeList.item(0);
//
//            if (paramDataNode.getNodeType() == Node.ELEMENT_NODE) {
//                Element paramDataElement = (Element) paramDataNode;
//                currentPageNum = paramDataElement.getElementsByTagName("pageNum").item(0).getTextContent();
//                total = paramDataElement.getElementsByTagName("total").item(0).getTextContent();
//            }
//
//            List<BookDto> bookList = new ArrayList<>();
//            for (int i = 0; i < itemNodeList.getLength(); i++) {
//                Node itemNode = itemNodeList.item(i);
//                if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
//                    Element itemElement = (Element) itemNode;
//                    String title = itemElement.getElementsByTagName("title_info").item(0).getTextContent();
//                    String author = itemElement.getElementsByTagName("author_info").item(0).getTextContent();
//                    String pub = itemElement.getElementsByTagName("pub_info").item(0).getTextContent();
//                    String isbn = itemElement.getElementsByTagName("isbn").item(0).getTextContent();
//                    String img = itemElement.getElementsByTagName("image_url").item(0).getTextContent();
//
//                    BookDto bookDto = new BookDto();
//                    bookDto.setSheetBooktitle(title);
//                    bookDto.setSheetBookauthor(author);
//                    bookDto.setSheetBookpublisher(pub);
//                    bookDto.setSheetBookisbn(isbn);
//                    bookDto.setSheetBookimgname(img);
//                    bookList.add(bookDto);
//                }
//            }
//
//            int totalPageNum = Integer.parseInt(total) == 1 ? 0 : (Integer.parseInt(total) / pageSize) + 1;
//            log.info("bookList = {}", bookList);
//            log.info("total = {}", total);
////            log.info("totalPageNum = {}", totalPageNum);
//            log.info("currentPageNum = {}", currentPageNum);
//
//            Map<String, Object> searchResult = new HashMap<>();
//            searchResult.put("bookList", bookList);
//            searchResult.put("total", total);
//            searchResult.put("totalPageNum", totalPageNum);
//            searchResult.put("currentPageNum", currentPageNum);
//
//            return searchResult;
//
//        } catch (ParserConfigurationException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } catch (SAXException e) {
//            throw new RuntimeException(e);
//        }

//        return result;
//    }

}

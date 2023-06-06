package com.bookitaka.NodeulProject.request;

import com.bookitaka.NodeulProject.sheet.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public interface RequestService {


    public Map<String, Object> searchBook(String keyword, String authorSearch, Integer pageNum);

}

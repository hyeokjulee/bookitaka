package com.bookitaka.NodeulProject.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public interface RequestService {
    void registerRequest(Request request);
    //    List<Faq> getAllFaq();
    Optional<Request> getOneRequest(Long RequestNo);
    void modifyRequest(Request requestModified);
    void removeRequest(Request request);

    Page<Request> getAllRequestByRequestIsdone(int requestIsdone, Pageable pageable);

    public Map<String, Object> searchBook(String keyword, String authorSearch, Integer pageNum);

}

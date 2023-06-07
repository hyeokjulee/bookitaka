package com.bookitaka.NodeulProject.sheet.mysheet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MysheetServiceImpl implements MysheetService{

    private final MysheetRepository mysheetRepository;

    @Override
    public List<Mysheet> getAllMysheetByEmail(String memberEmail) {
        return null;
    }

    @Override
    public List<Mysheet> getAllMysheetAvailableByEmail(String memberEmail) {
        return null;
    }

    @Override
    public List<Mysheet> getAllMysheetForAdmin() {
        return null;
    }
}

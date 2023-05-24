package com.bookitaka.NodeulProject.manual.service;

import com.bookitaka.NodeulProject.manual.domain.entity.Manual;
import com.bookitaka.NodeulProject.manual.repository.ManualRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManualService {

    @Autowired
    private ManualRepository manualRepository;

    public void manualwrite(Manual manual){

        manualRepository.save(manual);
    }
}

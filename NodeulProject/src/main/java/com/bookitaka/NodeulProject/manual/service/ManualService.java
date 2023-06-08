package com.bookitaka.NodeulProject.manual.service;

import com.bookitaka.NodeulProject.manual.domain.entity.Manual;
import com.bookitaka.NodeulProject.manual.dto.ManualDto;
import com.bookitaka.NodeulProject.manual.repository.ManualRepository;
import com.bookitaka.NodeulProject.notice.domain.entity.Notice;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ManualService {

    private ManualRepository manualRepository;

    @Transactional
    public List<ManualDto> getManualList(){
        List<Manual> manualEntites = manualRepository.findAll(Sort.by(Sort.Direction.DESC, "manualNo"));
        List<ManualDto> manualDtoList = new ArrayList<>();

        for(Manual manual : manualEntites){
            ManualDto manualDto = ManualDto.builder()
                    .manualNo(manual.getManualNo())
                    .manualTitle(manual.getManualTitle())
                    .manualContent(manual.getManualContent())
                    .build();

            manualDtoList.add(manualDto);
        }
        return manualDtoList;
    }

    @Transactional
    public Integer registerManual(ManualDto manualDto){
        return manualRepository.save(manualDto.toEntity()).getManualNo();

    }

    @Transactional
    public ManualDto getManual(Integer manualNo){

        Optional<Manual> manualEntityWrapper = manualRepository.findById(manualNo);
        Manual manual = manualEntityWrapper.get();

        ManualDto manualDto = ManualDto.builder()
                .manualNo(manual.getManualNo())
                .manualTitle(manual.getManualTitle())
                .manualContent(manual.getManualContent())
                .build();

        return manualDto;
    }

    @Transactional
    public void deleteManual(Integer manualNo){
        manualRepository.deleteById(manualNo);
    }
}

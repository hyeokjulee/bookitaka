package com.bookitaka.NodeulProject.manual.service;

import com.bookitaka.NodeulProject.manual.domain.entity.Manual;
import com.bookitaka.NodeulProject.manual.dto.ManualDto;
import com.bookitaka.NodeulProject.manual.repository.ManualRepository;
import com.bookitaka.NodeulProject.notice.domain.entity.Notice;
import com.bookitaka.NodeulProject.notice.dto.NoticeDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<ManualDto> getManualList(Pageable pageable) {
        Page<Manual> manualEntities = manualRepository.findAll(pageable);
        return manualEntities.map(this::convertEntityToDto);
    }

/*    public List<ManualDto> getManualList(){
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
    }*/

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
    public Page<ManualDto> searchManual(String keyword, Pageable pageable) {
        Page<Manual> manualEntities = manualRepository.findByManualTitleContainingOrManualContentContaining(keyword, keyword, pageable);

        if (manualEntities.isEmpty()) {
            return Page.empty(); // 빈 페이지 반환
        }

        return manualEntities.map(this::convertEntityToDto);
    }

    @Transactional
    public void deleteManual(Integer manualNo){
        manualRepository.deleteById(manualNo);
    }

    private ManualDto convertEntityToDto(Manual manual) {
        return ManualDto.builder()
                .manualNo(manual.getManualNo())
                .manualTitle(manual.getManualTitle())
                .manualContent(manual.getManualContent())
                .build();

    }
}

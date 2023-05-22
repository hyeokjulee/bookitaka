package com.bookitaka.NodeulProject.sheet;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class AgeGroupRepositoryTest {

    @Autowired
    AgeGroupRepository ageGroupRepository;


    @Test
    void AllAgeGroupTest() {
        int count = 0;

        List<String> ageGroups = ageGroupRepository.findAllSheetAgegroupName();

        for (String ageGroup: ageGroups) {
            log.info("AgeGroup = {}", ageGroup);
            count++;
        }
        Assertions.assertThat(count).isGreaterThan(3);
    }

}
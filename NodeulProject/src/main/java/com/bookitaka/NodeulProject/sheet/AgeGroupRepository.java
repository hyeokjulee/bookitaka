package com.bookitaka.NodeulProject.sheet;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgeGroupRepository extends JpaRepository<SheetAgegroup, Long> {

    SheetAgegroup findSheetAgegroupByName(String ageGroupName);

    List<String> findAllSheetAgegroupName();
}

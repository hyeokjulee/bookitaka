package com.bookitaka.NodeulProject.sheet;

import org.springframework.data.jpa.repository.JpaRepository;


public interface AgeGroupRepository extends JpaRepository<SheetAgegroup, Long> {

    SheetAgegroup findBySheetAgegroupName(String SheetAgegroupName);

}

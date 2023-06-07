package com.bookitaka.NodeulProject.sheet.mysheet;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MysheetRepository extends JpaRepository<Mysheet, Long> {

}

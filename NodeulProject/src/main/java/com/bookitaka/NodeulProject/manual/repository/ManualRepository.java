package com.bookitaka.NodeulProject.manual.repository;

import com.bookitaka.NodeulProject.manual.domain.entity.Manual;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManualRepository extends JpaRepository<Manual,Integer> {
}

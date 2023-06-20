package com.bookitaka.NodeulProject.sheet;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<SheetGenre, Long> {

    SheetGenre findTopBySheetGenreName(String sheetGenreName);

}



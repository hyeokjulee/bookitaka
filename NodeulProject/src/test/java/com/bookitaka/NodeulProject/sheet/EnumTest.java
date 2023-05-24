package com.bookitaka.NodeulProject.sheet;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class EnumTest {

    @Test
    void EnumTest() {

        log.info("Enum = {}", SearchTypes.TITLE);
        log.info("EnumType = {}", SearchTypes.TITLE.equals("제목"));

    }


}

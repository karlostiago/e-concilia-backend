package com.ctsousa.econcilia.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DataUtilTest {

    @Test
    void deveRetornarDiaMesQuandoPassarLocalDate() {
        var localDate = LocalDate.of(2024, 1, 12);
        assertEquals("12/01", DataUtil.diaMes(localDate));
    }

    @Test
    void naoDeveRetornarDiaMesQuandoLocalDateNull() {
        assertNull(DataUtil.diaMes(null));
    }
}

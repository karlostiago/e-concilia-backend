package com.ctsousa.econcilia.util;

import com.ctsousa.econcilia.model.dto.PeriodoDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

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

    @Test
    void deveCalcular() {
        List<PeriodoDTO> periodos = DataUtil.periodos(LocalDate.of(2024, 1, 1), 60);
        Assertions.assertEquals(2, periodos.size());
    }
}

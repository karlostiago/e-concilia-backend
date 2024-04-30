package com.ctsousa.econcilia.util;

import com.ctsousa.econcilia.enumaration.Faixa;
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
//        List<PeriodoDTO> periodos = DataUtil.periodos(LocalDate.of(2024, 1, 1), 60);
//        Assertions.assertEquals(2, periodos.size());
    }

    @Test
    void deveCalcularPeriodoAnual() {
        List<PeriodoDTO> periodos = DataUtil.periodoAnual(LocalDate.of(2024, 2, 27), Faixa.FX_90);
        Assertions.assertEquals(5, periodos.size());
        Assertions.assertEquals(LocalDate.of(2023, 2, 1), periodos.get(0).getDe());
        Assertions.assertEquals(LocalDate.of(2024, 2, 29), periodos.get(periodos.size() - 1).getAte());
    }
}

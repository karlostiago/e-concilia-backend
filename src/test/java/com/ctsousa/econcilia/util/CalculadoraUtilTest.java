package com.ctsousa.econcilia.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.ctsousa.econcilia.util.CalculadoraUtil.multiplicar;
import static com.ctsousa.econcilia.util.CalculadoraUtil.somar;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculadoraUtilTest {

    @Test
    void deveSomar() {
        List<BigDecimal> valores = List.of(BigDecimal.valueOf(1D), BigDecimal.valueOf(2D));
        assertEquals(BigDecimal.valueOf(3D), somar(valores));
    }

    @Test
    void deveRetornarZeroQuandoListaValoresVazio() {
        List<BigDecimal> valores = new ArrayList<>();
        assertEquals(BigDecimal.ZERO, somar(valores));
    }

    @Test
    void deveRetornarZeroQuandoListaNula() {
        assertEquals(BigDecimal.valueOf(0D), somar(null));
    }

    @Test
    void deveMultiplicar() {
        assertEquals(new BigDecimal("3.200"), multiplicar(BigDecimal.valueOf(0.032), 100));
    }

    @Test
    void deveRetornarZeroQuandoFatorMultiplicadorZero() {
        assertEquals(new BigDecimal("0.000"), multiplicar(BigDecimal.valueOf(0.032), 0));
    }

    @Test
    void deveRetornarZeroQuandoValorNull() {
        assertEquals(BigDecimal.valueOf(0D), multiplicar(null, 100));
    }
}

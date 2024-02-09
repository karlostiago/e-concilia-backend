package com.ctsousa.econcilia.util;

import org.junit.jupiter.api.Test;

import static com.ctsousa.econcilia.util.StringUtil.*;
import static org.junit.jupiter.api.Assertions.*;

class StringUtilTest {

    @Test
    void deveRetornarSomenteNumero() {
        assertEquals("123", somenteNumero("a1b2c3"));
        assertEquals("123", somenteNumero("abc123"));
        assertEquals("123", somenteNumero("123abc"));
    }

    @Test
    void deveRetornarVazioQuandoNaoContiverNumeros() {
        assertEquals("", somenteNumero("abc"));
    }

    @Test
    void deveRetornarNullQuandoInformarNull() {
        assertNull(somenteNumero(null));
    }

    @Test
    void deveRetornarNullQuandoInformarVazio() {
        assertNull(somenteNumero(""));
    }

    @Test
    void deveTransformarCaracteresEmMaiuscula() {
        assertEquals("TESTE", maiuscula("teste"));
        assertEquals("TESTE", maiuscula("TeStE"));
        assertEquals("TESTE", maiuscula("tEstE"));
        assertEquals("TESTE", maiuscula("testE"));
    }

    @Test
    void naoDeveTransformarCaracterEmMaiusculaQuandoInformarNull() {
        assertNull(maiuscula(null));
    }

    @Test
    void naoDeveTransformarCaracterEmMaiusculaQuandoInformarVazio() {
        assertNull(maiuscula(""));
    }

    @Test
    void deveValidarStringQuandoTiverValorValido() {
        assertTrue(temValor("tem valor"));
    }

    @Test
    void deveValidarStringQuandoNaoTiverValor() {
        assertTrue(naoTemValor(""));
        assertTrue(naoTemValor("undefined"));
        assertTrue(naoTemValor("null"));
        assertTrue(naoTemValor(null));
        assertTrue(naoTemValor("UNDEFINED"));
        assertTrue(naoTemValor("Undefined"));
    }
}

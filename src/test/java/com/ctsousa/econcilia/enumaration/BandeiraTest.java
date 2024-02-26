package com.ctsousa.econcilia.enumaration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BandeiraTest {

    @Test
    void deveRetornarBandeiraPorDescricao() {
        Bandeira bandeira = Bandeira.porDescricao("hipercard");

        Assertions.assertNotNull(bandeira);
        Assertions.assertEquals(Bandeira.HIPERCARD, bandeira);
        Assertions.assertEquals(Bandeira.HIPERCARD.getDescricao(), bandeira.getDescricao());
    }

    @Test
    void deveRetornarNullQuandoNaoExistirDescricao() {
        Assertions.assertNull(Bandeira.porDescricao("hiper"));
    }
}

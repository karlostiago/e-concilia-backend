package com.ctsousa.econcilia.enumaration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MetodoPagamentoTest {

    @Test
    void deveRetornarBandeiraPorDescricao() {
        MetodoPagamento metodoPagamento = MetodoPagamento.porDescricao("crédito");

        Assertions.assertNotNull(metodoPagamento);
        Assertions.assertEquals(MetodoPagamento.CREDIT, metodoPagamento);
        Assertions.assertEquals(MetodoPagamento.CREDIT.getDescricao(), metodoPagamento.getDescricao());
    }

    @Test
    void deveRetornarNullQuandoNaoExistirDescricao() {
        Assertions.assertNull(Bandeira.porDescricao("lojas"));
    }
}

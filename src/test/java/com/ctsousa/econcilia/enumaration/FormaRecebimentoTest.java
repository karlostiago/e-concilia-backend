package com.ctsousa.econcilia.enumaration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FormaRecebimentoTest {

    @Test
    void deveRetornarFormaPagamentoPorDescricao() {
        FormaRecebimento formaRecebimento = FormaRecebimento.porDescricao("loja");

        Assertions.assertNotNull(formaRecebimento);
        Assertions.assertEquals(FormaRecebimento.LOJA, formaRecebimento);
        Assertions.assertEquals(FormaRecebimento.LOJA.getDescricao(), formaRecebimento.getDescricao());
    }

    @Test
    void deveRetornarNullQuandoNaoExistirDescricao() {
        Assertions.assertNull(Bandeira.porDescricao("lojas"));
    }
}

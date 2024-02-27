package com.ctsousa.econcilia.enumaration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TipoNotificacaoTest {

    @Test
    void deveRetornarTipoNotificacaoPorCodigo() {
        TipoNotificacao tipoNotificacao = TipoNotificacao.porCodigo(1);

        Assertions.assertNotNull(tipoNotificacao);
        Assertions.assertEquals(TipoNotificacao.GLOBAL, tipoNotificacao);
        Assertions.assertEquals(TipoNotificacao.GLOBAL.getCodigo(), tipoNotificacao.getCodigo());
    }

    @Test
    void deveRetornarNullQuandoNaoExistirCodigo() {
        Assertions.assertNull(TipoNotificacao.porCodigo(3));
    }
}

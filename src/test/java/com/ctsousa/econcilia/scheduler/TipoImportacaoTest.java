package com.ctsousa.econcilia.scheduler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TipoImportacaoTest {

    @Test
    void deveBuscarTipoImportacao() {
        TipoImportacao tipoImportacao = TipoImportacao.porDescricao("ifood");
        Assertions.assertEquals(TipoImportacao.IFOOD, tipoImportacao);

        assert tipoImportacao != null;
        Assertions.assertEquals("IFOOD", tipoImportacao.getDescricao());
    }

    @Test
    void deveRetornarNullTipoImportacaoInexistente() {
        Assertions.assertNull(TipoImportacao.porDescricao("uber"));
    }
}

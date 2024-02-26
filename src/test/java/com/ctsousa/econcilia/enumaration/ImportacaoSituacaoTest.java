package com.ctsousa.econcilia.enumaration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ImportacaoSituacaoTest {

    @Test
    void deveRetornarImportacaoSituacaoPorDescricao() {
        ImportacaoSituacao importacaoSituacao = ImportacaoSituacao.porDescricao("agendado");

        Assertions.assertNotNull(importacaoSituacao);
        Assertions.assertEquals(ImportacaoSituacao.AGENDADO, importacaoSituacao);
        Assertions.assertEquals(ImportacaoSituacao.AGENDADO.getDescricao(), importacaoSituacao.getDescricao());
    }

    @Test
    void deveRetornarNullQuandoNaoExistirDescricao() {
        Assertions.assertNull(ImportacaoSituacao.porDescricao("agenda"));
    }
}

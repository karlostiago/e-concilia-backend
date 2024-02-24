package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.ApplicationIntegrationTest;
import com.ctsousa.econcilia.enumaration.ImportacaoSituacao;
import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Importacao;
import com.ctsousa.econcilia.model.Operadora;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

class ImportacaoServiceTest extends ApplicationIntegrationTest {

    @Autowired
    private ImportacaoService importacaoService;

    @BeforeEach
    void setup() {
        criarMassaDeDados();
    }

    @Test
    void deveAgendar() {
        Importacao importacao = criarImportacao();

        Assertions.assertNotNull(importacaoService.agendar(importacao).getId());
    }

    @Test
    void deveRetornarNotificacaoExceptionQuandoExistirAgendamentoProgramado() {
        importacaoRepository.save(criarImportacao());

        Importacao importacao = criarImportacao();

        var thrown = Assertions.assertThrows(NotificacaoException.class, () -> importacaoService.agendar(importacao));

        Assertions.assertEquals("Já existe uma empresa com operadora selecionada com agendamento programado. Aguarde a execução do agendamento para realizar um novo agendamento para está empresa e operadora.", thrown.getMessage());
    }

    @Test
    void deveBuscarImportacaoAgendadas() {
        importacaoRepository.save(criarImportacao());
        Assertions.assertEquals(1, importacaoService.buscarPorSituacaoAgendada().size());
    }

    @Test
    void deveAtualizarImportacao() {
        Importacao importacao = importacaoRepository.save(criarImportacao());
        importacao.setSituacao(ImportacaoSituacao.EM_PROCESSAMENTO);

        importacaoService.atualizaPara(importacao, ImportacaoSituacao.PROCESSADO);

        Assertions.assertEquals(ImportacaoSituacao.PROCESSADO, importacao.getSituacao());
    }

    private Importacao criarImportacao() {
        Empresa empresa = empresaRepository.findAll().get(0);
        Operadora operadora = operadoraRepository.findAll().get(0);

        Importacao importacao = new Importacao();
        importacao.setEmpresa(empresa);
        importacao.setOperadora(operadora);
        importacao.setDataInicial(LocalDate.now());
        importacao.setDataFinal(LocalDate.now().plusDays(30));
        importacao.setSituacao(ImportacaoSituacao.AGENDADO);

        return importacao;
    }
}

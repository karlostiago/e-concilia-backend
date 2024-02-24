package com.ctsousa.econcilia.scheduler;

import com.ctsousa.econcilia.ApplicationIntegrationTest;
import com.ctsousa.econcilia.enumaration.ImportacaoSituacao;
import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Importacao;
import com.ctsousa.econcilia.model.Integracao;
import com.ctsousa.econcilia.model.Operadora;
import com.ctsousa.econcilia.repository.VendaRepository;
import com.ctsousa.econcilia.scheduler.impl.ImportacaoSchedulerIfoodImpl;
import com.ctsousa.econcilia.service.ImportacaoService;
import com.ctsousa.econcilia.service.IntegracaoIfoodService;
import com.ctsousa.econcilia.service.IntegracaoService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

class ImportacaoSchedulerIfoodTest extends ApplicationIntegrationTest {

    private Scheduler scheduler;

    @Autowired
    private ImportacaoService importacaoService;

    @Autowired
    private IntegracaoService integracaoService;

    @Autowired
    private VendaRepository vendaRepository;

    @Mock
    private IntegracaoIfoodService integracaoIfoodService;

    @BeforeEach
    void setup() {
        ImportacaoSchedulerIfoodImpl importacaoServiceImpl = new ImportacaoSchedulerIfoodImpl(importacaoService, integracaoService, vendaRepository, integracaoIfoodService);
        importacaoServiceImpl.setHabilitar(true);
        scheduler = importacaoServiceImpl;

        criarSalvarEmpresa();
    }

    @AfterEach
    void destroy() {
        deletarMassaDeDados();
    }

    @Test
    void deveProcessar() {
        Importacao importacao = getImportacao();
        importacaoRepository.save(importacao);

        mockVendas();

        Integracao integracao = getIntegracao(importacao.getEmpresa(), importacao.getOperadora());
        integracaoRepository.save(integracao);

        scheduler.processar();

        Assertions.assertFalse(vendaRepository.findAll().isEmpty());
        Assertions.assertFalse(importacaoRepository.buscarPorSituacaoAgendada(ImportacaoSituacao.PROCESSADO).isEmpty());
    }

    private void mockVendas() {
        String codigoIntegracao = "123456";
        LocalDate dtInicial = LocalDate.of(2024, 2 ,24);
        LocalDate dtFinal = LocalDate.of(2024, 3 ,25);

        Mockito.when(integracaoIfoodService.pesquisarVendas(codigoIntegracao, null, null, null, dtInicial, dtFinal))
                .thenReturn(getVendas());
    }

    private Importacao getImportacao() {
        Empresa empresa = empresaRepository.findAll().get(0);

        Operadora operadora = getOperadora();
        operadora.setDescricao("ifood");
        operadoraRepository.save(operadora);

        Importacao importacao = new Importacao();
        importacao.setSituacao(ImportacaoSituacao.AGENDADO);
        importacao.setEmpresa(empresa);
        importacao.setOperadora(operadora);
        importacao.setDataInicial(LocalDate.now());
        importacao.setDataFinal(LocalDate.now().plusDays(90));
        return importacao;
    }
}

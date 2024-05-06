package com.ctsousa.econcilia.scheduler;

import com.ctsousa.econcilia.ApplicationIntegrationTest;
import com.ctsousa.econcilia.enumaration.ImportacaoSituacao;
import com.ctsousa.econcilia.enumaration.TipoParametro;
import com.ctsousa.econcilia.model.*;
import com.ctsousa.econcilia.repository.*;
import com.ctsousa.econcilia.scheduler.impl.ConsolidacaoSchedulerIfoodImpl;
import com.ctsousa.econcilia.scheduler.impl.ImportacaoProgramadaSchedulerIfoodImpl;
import com.ctsousa.econcilia.service.ImportacaoService;
import com.ctsousa.econcilia.service.IntegracaoIfoodService;
import com.ctsousa.econcilia.service.IntegracaoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

class ImportacaoSchedulerIfoodTest extends ApplicationIntegrationTest {

    private Scheduler scheduler;

    @Mock
    private ImportacaoService importacaoService;

    @Autowired
    private IntegracaoService integracaoService;

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private CancelamentoRepository cancelamentoRepository;

    @Autowired
    private AjusteVendaRepository ajusteVendaRepository;

    @Autowired
    private OcorrenciaRepository ocorrenciaRepository;

    @Mock
    private IntegracaoIfoodService integracaoIfoodService;

    @Mock
    private ConsolidacaoSchedulerIfoodImpl consolidacaoSchedulerIfood;

    @Mock
    private ConsolidadoRepository consolidadoRepository;

    @Mock
    private ParametroRepository parametroRepository;

    @BeforeEach
    void setup() {
        scheduler = new ImportacaoProgramadaSchedulerIfoodImpl(importacaoService, integracaoService, vendaRepository, integracaoIfoodService, cancelamentoRepository, ajusteVendaRepository, ocorrenciaRepository, consolidacaoSchedulerIfood, consolidadoRepository, parametroRepository);

        criarSalvarEmpresa();
    }

    @Test
    void deveProcessar() {
        Importacao importacao = getImportacao();
        importacaoRepository.save(importacao);

        mockVendas();

        Integracao integracao = getIntegracao(importacao.getEmpresa(), importacao.getOperadora());
        integracaoRepository.save(integracao);

        Parametro parametro = new Parametro();
        parametro.setAtivo(true);

        Mockito.when(importacaoService.buscarImportacoes())
                        .thenReturn(List.of(importacao));

        Mockito.when(parametroRepository.buscaParametroTipoEmpresaOperadora(TipoParametro.IMPORTACAO_PROGRAMADA, importacao.getEmpresa(), importacao.getOperadora()))
                        .thenReturn(parametro);

        scheduler.processar();

        Assertions.assertFalse(vendaRepository.findAll().isEmpty());
    }

    private void mockVendas() {
        String codigoIntegracao = "123456";
        LocalDate dtInicial = LocalDate.of(2024, 3 ,1);
        LocalDate dtFinal = LocalDate.of(2024, 3 ,25);

        Mockito.when(integracaoIfoodService.pesquisarVendas(codigoIntegracao, dtInicial, dtFinal))
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
        importacao.setDataInicial(LocalDate.of(2024, 2 ,24));
        importacao.setDataFinal(LocalDate.of(2024, 3 ,25));
        return importacao;
    }
}

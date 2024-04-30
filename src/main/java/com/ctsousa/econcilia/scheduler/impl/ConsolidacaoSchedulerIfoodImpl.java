package com.ctsousa.econcilia.scheduler.impl;

import com.ctsousa.econcilia.enumaration.TipoProcessador;
import com.ctsousa.econcilia.model.Contrato;
import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Integracao;
import com.ctsousa.econcilia.model.Operadora;
import com.ctsousa.econcilia.processor.Processador;
import com.ctsousa.econcilia.processor.ProcessadorFiltro;
import com.ctsousa.econcilia.repository.ConsolidadoRepository;
import com.ctsousa.econcilia.repository.IntegracaoRepository;
import com.ctsousa.econcilia.scheduler.Scheduler;
import com.ctsousa.econcilia.service.ContratoService;
import com.ctsousa.econcilia.service.OperadoraService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class ConsolidacaoSchedulerIfoodImpl implements Scheduler {

    private static final String IFOOD_OPERADORA = "ifood";

    private static final long DOIS_MINUTOS = 120000L;

    private final OperadoraService operadoraService;

    private final IntegracaoRepository integracaoRepository;

    private final ContratoService contratoService;

    private final ConsolidadoRepository consolidadoRepository;

    public ConsolidacaoSchedulerIfoodImpl(OperadoraService operadoraService, IntegracaoRepository integracaoRepository, ContratoService contratoService, ConsolidadoRepository consolidadoRepository) {
        this.operadoraService = operadoraService;
        this.integracaoRepository = integracaoRepository;
        this.contratoService = contratoService;
        this.consolidadoRepository = consolidadoRepository;
    }

    @Override
//    @Scheduled(fixedRate = DOIS_MINUTOS)
    public void processar() {
        Operadora operadora = operadoraService.buscarPorDescricao(IFOOD_OPERADORA);
        List<Contrato> contratos = contratoService.pesquisar(null, operadora.getId());

        List<Empresa> empresas = contratos.stream()
                .map(Contrato::getEmpresa)
                .toList();

        for (Empresa empresa : empresas) {
            prepararConsolidacaoVendas(empresa, LocalDate.now());
        }
    }

    public void processar(final Empresa empresa, final LocalDate periodo) {
        prepararConsolidacaoVendas(empresa, periodo);
    }

    private void prepararConsolidacaoVendas(final Empresa empresa, final LocalDate periodo) {
        List<Integracao> integracoes = integracaoRepository.findByEmpresa(empresa);
        for (Integracao integracao : integracoes) {
            executarConsolidacaoVendas(integracao, periodo);
        }
    }

    private void executarConsolidacaoVendas(Integracao integracao, LocalDate periodoInicial) {
        List<LocalDate> periodos = gerarPeriodos(periodoInicial);

        for (LocalDate periodo : periodos) {
            if (temConsolidacao(integracao, periodo)) continue;

            ProcessadorFiltro processadorFiltro = new ProcessadorFiltro(integracao, periodo, periodo);
            Processador processador = TipoProcessador.porOperadora(integracao.getOperadora());
            processador.processar(processadorFiltro, true, true);
        }
    }

    private boolean temConsolidacao(final Integracao integracao, LocalDate periodo) {
        return consolidadoRepository.existsConsolidacao(integracao.getEmpresa(), integracao.getOperadora(), periodo);
    }

    private List<LocalDate> gerarPeriodos(LocalDate periodo) {
        LocalDate primeiroDiaMes = periodo.withDayOfMonth(1);
        List<LocalDate> periodos = new ArrayList<>();

        while (!periodo.isBefore(primeiroDiaMes)) {
            periodos.add(periodo);
            periodo = periodo.minusDays(1);
        }

        Collections.sort(periodos);

        return periodos;
    }
}

package com.ctsousa.econcilia.scheduler.impl;

import com.ctsousa.econcilia.integration.ifood.IfoodGateway;
import com.ctsousa.econcilia.model.Importacao;
import com.ctsousa.econcilia.scheduler.ProcessoScheduler;
import com.ctsousa.econcilia.service.ImportacaoService;
import com.ctsousa.econcilia.service.IntegracaoService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ImportacaoSchedulerImpl implements ProcessoScheduler {

    private static final long QUINZE_MINUTOS = 900000L;

    private final ImportacaoService importacaoService;

    private final IfoodGateway ifoodGateway;

    private final IntegracaoService integracaoService;

    public ImportacaoSchedulerImpl(ImportacaoService importacaoService, IfoodGateway ifoodGateway, IntegracaoService integracaoService) {
        this.importacaoService = importacaoService;
        this.ifoodGateway = ifoodGateway;
        this.integracaoService = integracaoService;
    }

    @Override
    @Scheduled(fixedRate = QUINZE_MINUTOS)
    public void processar() {
        Importacao importacao = importacaoService.buscarPorSituacaoAgendada()
                .stream().findFirst().orElse(null);

        List<Periodo> periodos = new ArrayList<>();

        if (importacao != null) {
            log.info("Iniciando processamento para empresa {}, operadora {}, periodo inicial {}, periodo final {} "
                    ,importacao.getEmpresa().getRazaoSocial()
                    ,importacao.getOperadora().getDescricao()
                    ,importacao.getDataInicial()
                    ,importacao.getDataFinal());

            long totalDias = ChronoUnit.DAYS.between(importacao.getDataInicial(), importacao.getDataFinal());
            periodos = calcularPeriodo(importacao.getDataInicial(), totalDias);
        }

        if (periodos.isEmpty()) {
            log.info("Não foi encontrado nenhum periodo para ser processado.");
            return;
        }
    }

    private void executar() {

    }

    private List<Periodo> calcularPeriodo(final LocalDate periodoInicial, long totalDias) {
        List<Periodo> periodos = new ArrayList<>();
        LocalDate dtInicial = periodoInicial;

        while (totalDias > 0) {
            long diasNoPeriodo = Math.min(totalDias, 30);
            LocalDate periodoFinal = dtInicial.plusDays(diasNoPeriodo - 1);
            periodos.add(new Periodo(dtInicial, periodoFinal));
            dtInicial = periodoFinal.plusDays(1);
            totalDias -= diasNoPeriodo;
        }

        return periodos;
    }

    @Getter
    @AllArgsConstructor
    private static class Periodo {
        private LocalDate de;
        private LocalDate ate;
    }
}

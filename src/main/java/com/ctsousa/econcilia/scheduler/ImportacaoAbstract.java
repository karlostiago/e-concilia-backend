package com.ctsousa.econcilia.scheduler;

import com.ctsousa.econcilia.model.Importacao;
import com.ctsousa.econcilia.service.ImportacaoService;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class ImportacaoAbstract {

    private final ImportacaoService importacaoService;

    protected Importacao importacao;

    protected ImportacaoAbstract(ImportacaoService importacaoService) {
        this.importacaoService = importacaoService;
        this.buscarImportacao();
    }

    // @TODO existe um erro no calculo de peridos.
    protected List<ImportacaoAbstract.Periodo> calcularPeriodo(final LocalDate periodoInicial, long totalDias) {
        List<ImportacaoAbstract.Periodo> periodos = new ArrayList<>();
        LocalDate dtInicial = periodoInicial;
        boolean calculado = false;

        if (totalDias < 29) {
            periodos.add(new ImportacaoAbstract.Periodo(dtInicial, dtInicial.plusDays(totalDias)));
            calculado = true;
        }

        while (!calculado && totalDias > 0) {
            long diasNoPeriodo = Math.min(totalDias, 30);
            LocalDate periodoFinal = dtInicial.plusDays(diasNoPeriodo - 1);
            periodos.add(new ImportacaoAbstract.Periodo(dtInicial, periodoFinal));
            dtInicial = periodoFinal.plusDays(1);
            totalDias -= diasNoPeriodo;
            calculado = true;
        }

        return periodos;
    }

    public abstract TipoImportacao tipoImportacao();

    private void buscarImportacao() {
        importacao = importacaoService.buscarPorSituacaoAgendada()
                .stream()
                .filter(imp -> imp.getOperadora().getDescricao().equalsIgnoreCase(tipoImportacao().getDescricao()))
                .findFirst().orElse(null);
    }

    @Getter
    @AllArgsConstructor
    protected static class Periodo {
        private LocalDate de;
        private LocalDate ate;
    }
}

package com.ctsousa.econcilia.scheduler;

import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.model.Importacao;
import com.ctsousa.econcilia.model.Integracao;
import com.ctsousa.econcilia.service.ImportacaoService;
import com.ctsousa.econcilia.service.IntegracaoService;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public abstract class ImportacaoAbstract {

    private final ImportacaoService importacaoService;

    private final IntegracaoService integracaoService;

    protected Importacao importacao;

    protected List<Periodo> periodos;

    protected ImportacaoAbstract(ImportacaoService importacaoService, IntegracaoService integracaoService) {
        this.importacaoService = importacaoService;
        this.integracaoService = integracaoService;
        this.buscarImportacao();
        this.buscarPeriodos();
    }

    private void buscarPeriodos() {
        if (importacao != null) {
            periodos = new ArrayList<>();

            long totalDias = ChronoUnit.DAYS.between(importacao.getDataInicial(), importacao.getDataFinal());
            if (totalDias == 0) totalDias++;

            calcularPeriodo(importacao.getDataInicial(), totalDias);
        }
    }

    private void calcularPeriodo(LocalDate periodoInicial, long totalDias) {
        boolean executa = true;

        if (totalDias <= 30) {
            periodos.add(new Periodo(periodoInicial, periodoInicial.plusDays(totalDias)));
            executa = false;
        }

        while (executa) {
            LocalDate periodoFinal = periodoInicial.plusDays(30);

            if (!periodos.isEmpty()) {
                periodoInicial = periodos.get(periodos.size() - 1).getAte()
                        .plusDays(1);

                periodoFinal = periodoInicial.plusDays(totalDias < 30 ? totalDias : 30)
                        .minusDays(1);
            }

            periodos.add(new Periodo(periodoInicial, periodoFinal));
            totalDias -= 30;

            if (totalDias <= 0) {
                executa = false;
            }
        }
    }

    public abstract TipoImportacao tipoImportacao();

    public String getCodigoIntegracao() {
        var empresa = importacao.getEmpresa();
        var operadora = importacao.getOperadora();

        List<Integracao> integracoes = integracaoService.pesquisar(empresa.getId(), operadora.getId(), null);

        if (integracoes.isEmpty()) {
            throw new NotificacaoException(String.format("Nenhum codigo integracao encontrado para empresa %s e operadora %s", empresa.getRazaoSocial(), operadora.getDescricao()));
        }

        return integracoes.get(0).getCodigoIntegracao();
    }

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

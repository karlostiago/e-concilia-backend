package com.ctsousa.econcilia.scheduler;

import com.ctsousa.econcilia.model.Importacao;
import com.ctsousa.econcilia.model.Integracao;
import com.ctsousa.econcilia.model.Venda;
import com.ctsousa.econcilia.model.dto.PeriodoDTO;
import com.ctsousa.econcilia.service.ImportacaoService;
import com.ctsousa.econcilia.service.IntegracaoService;
import com.ctsousa.econcilia.util.DataUtil;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static com.ctsousa.econcilia.util.DataUtil.periodos;

public abstract class ImportacaoAbstract {

    private final ImportacaoService importacaoService;

    private final IntegracaoService integracaoService;

    protected Importacao importacao;

    protected List<PeriodoDTO> periodos;

    protected ImportacaoAbstract(ImportacaoService importacaoService, IntegracaoService integracaoService) {
        this.importacaoService = importacaoService;
        this.integracaoService = integracaoService;
    }

    public void executar() {
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
        periodos = periodos(periodoInicial, totalDias);
    }

    public abstract TipoImportacao tipoImportacao();

    public String getCodigoIntegracao() {
        var empresa = importacao.getEmpresa();
        var operadora = importacao.getOperadora();
        Integracao integracao = integracaoService.pesquisar(empresa, operadora);
        return integracao.getCodigoIntegracao();
    }

    private void buscarImportacao() {
        importacao = importacaoService.buscarPorSituacaoAgendada()
                .stream()
                .filter(imp -> imp.getOperadora().getDescricao().equalsIgnoreCase(tipoImportacao().getDescricao()))
                .findFirst().orElse(null);
    }
}

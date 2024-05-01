package com.ctsousa.econcilia.scheduler;

import com.ctsousa.econcilia.enumaration.ImportacaoSituacao;
import com.ctsousa.econcilia.model.*;
import com.ctsousa.econcilia.model.dto.PeriodoDTO;
import com.ctsousa.econcilia.repository.*;
import com.ctsousa.econcilia.service.ImportacaoService;
import com.ctsousa.econcilia.service.IntegracaoService;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.ctsousa.econcilia.util.DataUtil.obterPeriodoPorMesFechado;

@Slf4j
public abstract class ImportacaoAbstract {

    private final ImportacaoService importacaoService;

    private final IntegracaoService integracaoService;

    private final VendaRepository vendaRepository;

    private final AjusteVendaRepository ajusteVendaRepository;

    private final OcorrenciaRepository ocorrenciaRepository;

    private final CancelamentoRepository cancelamentoRepository;

    private final ConsolidadoRepository consolidadoRepository;

    protected Importacao importacao;

    protected List<PeriodoDTO> periodos;

    protected ImportacaoAbstract(ImportacaoService importacaoService, IntegracaoService integracaoService, VendaRepository vendaRepository, AjusteVendaRepository ajusteVendaRepository, OcorrenciaRepository ocorrenciaRepository, CancelamentoRepository cancelamentoRepository, ConsolidadoRepository consolidadoRepository) {
        this.importacaoService = importacaoService;
        this.integracaoService = integracaoService;
        this.vendaRepository = vendaRepository;
        this.ajusteVendaRepository = ajusteVendaRepository;
        this.ocorrenciaRepository = ocorrenciaRepository;
        this.cancelamentoRepository = cancelamentoRepository;
        this.consolidadoRepository = consolidadoRepository;
    }

    public void executar() {
        this.buscarImportacao();
        this.buscarPeriodos();
    }

    private void buscarPeriodos() {
        if (importacao != null) {
            periodos = obterPeriodoPorMesFechado(importacao.getDataInicial(), importacao.getDataFinal());
        }
    }

    public abstract TipoImportacao tipoImportacao();

    public String getCodigoIntegracao() {
        var empresa = importacao.getEmpresa();
        var operadora = importacao.getOperadora();
        Integracao integracao = integracaoService.pesquisar(empresa, operadora);
        return integracao.getCodigoIntegracao();
    }

    private void buscarImportacao() {
        importacao = importacaoService.buscarImportacoes()
                .stream()
                .filter(imp -> imp.getOperadora().getDescricao().equalsIgnoreCase(tipoImportacao().getDescricao()))
                .filter(imp -> !imp.getSituacao().equals(ImportacaoSituacao.PROCESSADO))
                .findFirst().orElse(null);
    }

    protected List<String> getPeriodoIds(final List<Venda> vendas) {
        return vendas.stream()
                .map(Venda::getPeriodoId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet())
                .stream().toList();
    }

    protected void salvarVendas(final List<Venda> vendas) {
        log.info("Quantidade de vendas encontradas {}", vendas.size());

        if (!vendas.isEmpty()) {
            log.info("Importando vendas na base de dados....");

            for (Venda venda : vendas) {
                venda.setEmpresa(importacao.getEmpresa());
                venda.setOperadora(importacao.getOperadora());
                vendaRepository.save(venda);
            }
        }
    }

    protected void salvarCancelamentos(final List<Cancelamento> cancelamentos) {
        log.info("Quantidade de cancelamento encontrados. {} ", cancelamentos.size());

        if (!cancelamentos.isEmpty()) {
            log.info("Importando cancelamentos.");
            cancelamentoRepository.saveAll(cancelamentos);
        }
    }

    protected void salvarAjusteVendas(final List<AjusteVenda> ajusteVendas) {
        log.info("Quantidade de ajuste vendas encontradas {}", ajusteVendas.size());

        if (!ajusteVendas.isEmpty()) {
            log.info("Importando ajuste vendas na base de dados....");
            ajusteVendas.forEach(ajusteVendaRepository::save);
        }
    }

    protected void salvarOcorrenciaDeVendas(final List<Ocorrencia> ocorrencias) {
        log.info("Quantidade de ocorrencias encontradas {}", ocorrencias.size());

        if (!ocorrencias.isEmpty()) {
            log.info("Importando ocorrencias na base de dados....");
            ocorrencias.forEach(ocorrenciaRepository::save);
        }
    }

    protected void deleteVendas(Empresa empresa, Operadora operadora, LocalDate periodoInicial, LocalDate periodoFinal) {
        List<Long> listCobrancaId = new ArrayList<>();
        List<Long> listPagamentoId = new ArrayList<>();

        List<Venda> vendas = vendaRepository.buscarPor(empresa, operadora, periodoInicial, periodoFinal);

        for (Venda venda : vendas) {
            listCobrancaId.add(venda.getCobranca().getId());
            listPagamentoId.add(venda.getPagamento().getId());
        }

        ajusteVendaRepository.deleteAjusteVendas(periodoFinal);
        ocorrenciaRepository.deleteOcorrencia(periodoFinal);

        vendaRepository.deleteVendas(empresa, operadora, periodoFinal);
        vendaRepository.deleteCobrancas(listCobrancaId);
        vendaRepository.deletePagamentos(listPagamentoId);

        consolidadoRepository.deleteConsolidados(empresa, operadora, periodoFinal);
    }

    protected boolean temVenda(Empresa empresa, Operadora operadora, LocalDate periodo) {
        return vendaRepository.existsVenda(empresa, operadora, periodo);
    }
}

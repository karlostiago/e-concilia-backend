package com.ctsousa.econcilia.scheduler.impl;

import com.ctsousa.econcilia.enumaration.ImportacaoSituacao;
import com.ctsousa.econcilia.model.AjusteVenda;
import com.ctsousa.econcilia.model.Cancelamento;
import com.ctsousa.econcilia.model.Ocorrencia;
import com.ctsousa.econcilia.model.Venda;
import com.ctsousa.econcilia.model.dto.PeriodoDTO;
import com.ctsousa.econcilia.repository.AjusteVendaRepository;
import com.ctsousa.econcilia.repository.CancelamentoRepository;
import com.ctsousa.econcilia.repository.OcorrenciaRepository;
import com.ctsousa.econcilia.repository.VendaRepository;
import com.ctsousa.econcilia.scheduler.ImportacaoAbstract;
import com.ctsousa.econcilia.scheduler.Scheduler;
import com.ctsousa.econcilia.scheduler.TipoImportacao;
import com.ctsousa.econcilia.service.ImportacaoService;
import com.ctsousa.econcilia.service.IntegracaoIfoodService;
import com.ctsousa.econcilia.service.IntegracaoService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ImportacaoSchedulerIfoodImpl extends ImportacaoAbstract implements Scheduler {

    private static final long QUINZE_MINUTOS = 900000L;

    private final ImportacaoService importacaoService;

    private final IntegracaoIfoodService integracaoIfoodService;

    private final VendaRepository vendaRepository;

    private final CancelamentoRepository cancelamentoRepository;

    private final AjusteVendaRepository ajusteVendaRepository;

    private final OcorrenciaRepository ocorrenciaRepository;

    private String codigoIntegracao;

    @Setter
    @Value("${importacao_habilitar}")
    private boolean habilitar;

    public ImportacaoSchedulerIfoodImpl(ImportacaoService importacaoService, IntegracaoService integracaoService, VendaRepository vendaRepository, IntegracaoIfoodService integracaoIfoodService, CancelamentoRepository cancelamentoRepository, AjusteVendaRepository ajusteVendaRepository, OcorrenciaRepository ocorrenciaRepository) {
        super(importacaoService, integracaoService);
        this.importacaoService = importacaoService;
        this.vendaRepository = vendaRepository;
        this.integracaoIfoodService = integracaoIfoodService;
        this.cancelamentoRepository = cancelamentoRepository;
        this.ajusteVendaRepository = ajusteVendaRepository;
        this.ocorrenciaRepository = ocorrenciaRepository;
    }

    @Override
    @Scheduled(fixedRate = QUINZE_MINUTOS)
    public void processar() {
        if (!habilitar) {
            log.info("O processo de importação não está habilitado.");
            return;
        }

        executar();

        if (periodos == null || periodos.isEmpty()) {
            log.info("Nenhum periodo encontrado para ser importado.");
            return;
        }

        log.info("Iniciando processamento para empresa {}, operadora {}, periodo inicial {}, periodo final {} ", importacao.getEmpresa().getRazaoSocial(), importacao.getOperadora().getDescricao(), importacao.getDataInicial(), importacao.getDataFinal());

        codigoIntegracao = getCodigoIntegracao();
        importacaoService.atualizaPara(importacao, ImportacaoSituacao.EM_PROCESSAMENTO);
        importar(periodos);

        log.info("Importação concluída com sucesso.");
    }

    private void importar(final List<PeriodoDTO> periodos) {
        for (PeriodoDTO periodo : periodos) {
            buscarSalvarVendas(periodo);
            buscarSalvarAjusteVendas(periodo);
            buscarSalvarOcorrencias(periodo);
        }
        log.info("Atualizando situação da importação ...");
        importacaoService.atualizaPara(importacao, ImportacaoSituacao.PROCESSADO);
    }

    private void buscarSalvarVendas(final PeriodoDTO periodo) {
        log.info("Pesquisando as vendas para empresa {}, operadora {}, no periodo de {} ate {}", importacao.getEmpresa().getRazaoSocial(), importacao.getOperadora().getDescricao(), periodo.getDe(), periodo.getAte());
        List<Venda> vendas = integracaoIfoodService.pesquisarVendas(codigoIntegracao, null, null, null, periodo.getDe(), periodo.getAte());
        if (!vendas.isEmpty()) {
            salvarVendas(vendas);
        }
    }

    private void buscarSalvarAjusteVendas(final PeriodoDTO periodo) {
        log.info("Pesquisando ajuste de vendas para empresa {}, operadora {}, no periodo de {} ate {}", importacao.getEmpresa().getRazaoSocial(), importacao.getOperadora().getDescricao(), periodo.getDe(), periodo.getAte());
        List<AjusteVenda> ajusteVendas = integracaoIfoodService.pesquisarAjusteVendas(codigoIntegracao, periodo.getDe(), periodo.getAte());
        if (!ajusteVendas.isEmpty()) {
            log.info("Quantidade de ajuste vendas encontradas {}", ajusteVendas.size());
            log.info("Importando ajuste vendas na base de dados....");
            ajusteVendas.forEach(ajusteVendaRepository::save);
        }
    }

    private void buscarSalvarOcorrencias(final PeriodoDTO periodo) {
        log.info("Pesquisando ocorrencias para empresa {}, operadora {}, no periodo de {} ate {}", importacao.getEmpresa().getRazaoSocial(), importacao.getOperadora().getDescricao(), periodo.getDe(), periodo.getAte());
        List<Ocorrencia> ocorrencias = integracaoIfoodService.pesquisarOcorrencias(codigoIntegracao, periodo.getDe(), periodo.getAte());
        if (!ocorrencias.isEmpty()) {
            log.info("Quantidade de ocorrencias encontradas {}", ocorrencias.size());
            log.info("Importando ocorrencias na base de dados....");
            ocorrencias.forEach(ocorrenciaRepository::save);
        }
    }

    private void salvarVendas(final List<Venda> vendas) {
        log.info("Quantidade de vendas encontradas {}", vendas.size());
        log.info("Importando vendas na base de dados....");

        List<String> periodosBuscados = new ArrayList<>();

        for (Venda venda : vendas) {
            venda.setEmpresa(importacao.getEmpresa());
            venda.setOperadora(importacao.getOperadora());

            vendaRepository.save(venda);

            if (!periodosBuscados.contains(venda.getPeriodoId())) {
                salvarCancelamento(venda);
            }

            periodosBuscados.add(venda.getPeriodoId());
        }
    }

    private void salvarCancelamento(final Venda venda) {
        log.info("Buscando cancelamentos ...");
        List<Cancelamento> cancelamentos = integracaoIfoodService.pesquisarCancelamentos(codigoIntegracao, venda.getPeriodoId());

        log.info("Quantidade de cancelamento encontrados. {} ", cancelamentos.size());

        if (!cancelamentos.isEmpty()) {
            log.info("Importando cancelamentos.");
            cancelamentoRepository.saveAll(cancelamentos);
        }
    }

    @Override
    public TipoImportacao tipoImportacao() {
        return TipoImportacao.IFOOD;
    }
}

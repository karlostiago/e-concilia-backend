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

import java.util.List;

@Slf4j
@Component
public class ImportacaoProgramadaSchedulerIfoodImpl extends ImportacaoAbstract implements Scheduler {

    private final ImportacaoService importacaoService;

    private final IntegracaoIfoodService integracaoIfoodService;

    private final ConsolidacaoSchedulerIfoodImpl consolidacaoScheduler;

    private String codigoIntegracao;

    @Setter
    @Value("${importacao_habilitar}")
    private boolean habilitar;

    public ImportacaoProgramadaSchedulerIfoodImpl(ImportacaoService importacaoService, IntegracaoService integracaoService, VendaRepository vendaRepository, IntegracaoIfoodService integracaoIfoodService, CancelamentoRepository cancelamentoRepository, AjusteVendaRepository ajusteVendaRepository, OcorrenciaRepository ocorrenciaRepository, ConsolidacaoSchedulerIfoodImpl consolidacaoScheduler) {
        super(importacaoService, integracaoService, vendaRepository, ajusteVendaRepository, ocorrenciaRepository, cancelamentoRepository);
        this.importacaoService = importacaoService;
        this.integracaoIfoodService = integracaoIfoodService;
        this.consolidacaoScheduler = consolidacaoScheduler;
    }

    /**
     * Este processo sera executado a cada 15 minutos
     */
    @Override
    @Scheduled(cron = "0 */15 * * * *")
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

        periodos = null;
        importacao = null;
    }

    private void importar(final List<PeriodoDTO> periodos) {
        for (PeriodoDTO periodo : periodos) {
            importarVendas(periodo);
            importarAjusteVendas(periodo);
            importarOcorrencias(periodo);
            importarCancelamentos(periodo);
            consolidacaoScheduler.processar(importacao.getEmpresa(), periodo.getAte());
        }
        log.info("Atualizando situação da importação ...");
        importacaoService.atualizaPara(importacao, ImportacaoSituacao.PROCESSADO);
    }

    private void importarVendas(final PeriodoDTO periodo) {
        log.info("Pesquisando as vendas para empresa {}, operadora {}, no periodo de {} ate {}", importacao.getEmpresa().getRazaoSocial(), importacao.getOperadora().getDescricao(), periodo.getDe(), periodo.getAte());
        List<Venda> vendas = integracaoIfoodService.pesquisarVendas(codigoIntegracao, periodo.getDe(), periodo.getAte());
        salvarVendas(vendas);
    }

    private void importarAjusteVendas(final PeriodoDTO periodo) {
        log.info("Pesquisando ajuste de vendas para empresa {}, operadora {}, no periodo de {} ate {}", importacao.getEmpresa().getRazaoSocial(), importacao.getOperadora().getDescricao(), periodo.getDe(), periodo.getAte());
        List<AjusteVenda> ajusteVendas = integracaoIfoodService.pesquisarAjusteVendas(codigoIntegracao, periodo.getDe(), periodo.getAte());
        salvarAjusteVendas(ajusteVendas);
    }

    private void importarOcorrencias(final PeriodoDTO periodo) {
        log.info("Pesquisando ocorrencias para empresa {}, operadora {}, no periodo de {} ate {}", importacao.getEmpresa().getRazaoSocial(), importacao.getOperadora().getDescricao(), periodo.getDe(), periodo.getAte());
        List<Ocorrencia> ocorrencias = integracaoIfoodService.pesquisarOcorrencias(codigoIntegracao, periodo.getDe(), periodo.getAte());
        salvarOcorrenciaDeVendas(ocorrencias);
    }

    private void importarCancelamentos(PeriodoDTO periodo) {
        log.info("Pesquisando cancelamentos no periodo de {} ate {}", periodo.getDe(), periodo.getAte());
        List<Cancelamento> cancelamentos = integracaoIfoodService.pesquisarCancelamentos(codigoIntegracao, periodo.getDe(), periodo.getAte());
        salvarCancelamentos(cancelamentos);
    }

    @Override
    public TipoImportacao tipoImportacao() {
        return TipoImportacao.IFOOD;
    }
}

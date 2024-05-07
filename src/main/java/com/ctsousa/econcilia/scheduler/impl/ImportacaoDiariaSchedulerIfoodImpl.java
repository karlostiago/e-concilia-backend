package com.ctsousa.econcilia.scheduler.impl;

import com.ctsousa.econcilia.enumaration.ImportacaoSituacao;
import com.ctsousa.econcilia.enumaration.TipoParametro;
import com.ctsousa.econcilia.model.*;
import com.ctsousa.econcilia.model.dto.PeriodoDTO;
import com.ctsousa.econcilia.repository.*;
import com.ctsousa.econcilia.scheduler.ImportacaoAbstract;
import com.ctsousa.econcilia.scheduler.Scheduler;
import com.ctsousa.econcilia.scheduler.TipoImportacao;
import com.ctsousa.econcilia.service.ContratoService;
import com.ctsousa.econcilia.service.IntegracaoIfoodService;
import com.ctsousa.econcilia.service.OperadoraService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class ImportacaoDiariaSchedulerIfoodImpl extends ImportacaoAbstract implements Scheduler {

    private final OperadoraService operadoraService;

    private final ContratoService contratoService;

    private final IntegracaoRepository integracaoRepository;

    private final IntegracaoIfoodService integracaoIfoodService;

    private final ConsolidacaoSchedulerIfoodImpl consolidacaoScheduler;

    private final ParametroRepository parametroRepository;

    private final ImportacaoRepository importacaoRepository;

    @Setter
    @Value("${importacao_habilitar}")
    private boolean habilitar;

    public ImportacaoDiariaSchedulerIfoodImpl(OperadoraService operadoraService, ContratoService contratoService, IntegracaoRepository integracaoRepository, VendaRepository vendaRepository, IntegracaoIfoodService integracaoIfoodService, CancelamentoRepository cancelamentoRepository, AjusteVendaRepository ajusteVendaRepository, OcorrenciaRepository ocorrenciaRepository, ConsolidadoRepository consolidadoRepository, ConsolidacaoSchedulerIfoodImpl consolidacaoScheduler, ParametroRepository parametroRepository, ImportacaoRepository importacaoRepository) {
        super(null, null, vendaRepository, ajusteVendaRepository, ocorrenciaRepository, cancelamentoRepository, consolidadoRepository);
        this.operadoraService = operadoraService;
        this.contratoService = contratoService;
        this.integracaoRepository = integracaoRepository;
        this.integracaoIfoodService = integracaoIfoodService;
        this.consolidacaoScheduler = consolidacaoScheduler;
        this.parametroRepository = parametroRepository;
        this.importacaoRepository = importacaoRepository;
    }

    /**
     * Este processo sera executado todos os dias as 23h59m59s
     */
    @Override
    @Scheduled(cron = "5 0 * * * *")
    public void processar() {
        Operadora operadora = operadoraService.buscarPorDescricao(tipoImportacao().getDescricao());
        List<Contrato> contratos = contratoService.pesquisar(null, operadora.getId());

        List<Empresa> empresas = contratos.stream()
                .map(Contrato::getEmpresa)
                .toList();

        if (empresas.isEmpty()) return;

        log.info("Iniciando processo de importação diária concluída com sucesso.");

        for (Empresa empresa : empresas) {
            importacao = new Importacao();
            importacao.setEmpresa(empresa);
            importacao.setOperadora(operadora);

            Parametro parametro = parametroRepository.buscaParametroTipoEmpresaOperadora(tipoParametro(), empresa, operadora);
            if (parametro != null && parametro.isAtivo()) {
                prepararImportacaoVendas(empresa);
                log.info("Processo de importação diária concluída com sucesso.");
            } else {
                log.info("O processo de importação não está habilitado.");
            }
        }
    }

    @Override
    public TipoParametro tipoParametro() {
        return TipoParametro.IMPORTACAO_DIARIA;
    }

    private void prepararImportacaoVendas(Empresa empresa) {
        List<Integracao> integracoes = integracaoRepository.findByEmpresa(empresa);
        for (Integracao integracao : integracoes) {
            executarImportacao(integracao);
        }
    }

    private void executarImportacao(Integracao integracao) {
        LocalDate periodo = LocalDate.now().minusDays(1);
        LocalDate periodoInicial = periodo.withDayOfMonth(1);
        LocalDate periodoFinal = periodo;

        if (periodo.getDayOfMonth() == 1) {
            periodo = periodo.minusDays(1);
            periodoInicial = periodo.withDayOfMonth(1);
            periodoFinal = periodo;
        }

        boolean temVenda = temVenda(integracao.getEmpresa(), integracao.getOperadora(), periodo);

        if (temVenda) {
            deleteVendas(integracao.getEmpresa(), integracao.getOperadora(), periodoInicial, periodoFinal);
        }

        List<Venda> vendas = importarVendas(integracao, new PeriodoDTO(periodoInicial, periodoFinal));

        if (!vendas.isEmpty()) {
            importarCancelamentos(integracao, new PeriodoDTO(periodoInicial, periodoFinal));
            importarAjusteVendas(integracao, new PeriodoDTO(periodoInicial, periodoFinal));
            importarOcorrencias(integracao, new PeriodoDTO(periodoInicial, periodoFinal));
            consolidacaoScheduler.processar(integracao.getEmpresa(), periodoFinal);
            atualizarStatusImportacao(periodoInicial, periodoFinal);
        }
    }

    private void atualizarStatusImportacao(LocalDate periodoInicial, LocalDate periodoFinal) {
        this.importacao.setDataInicial(periodoInicial);
        this.importacao.setDataFinal(periodoFinal);
        this.importacao.setSituacao(ImportacaoSituacao.PROCESSADO);
        this.importacao.setExecutado(LocalDateTime.now());
        this.importacaoRepository.save(importacao);
    }

    private List<Venda> importarVendas(Integracao integracao, PeriodoDTO periodo) {
        log.info("Pesquisando vendas para empresa {}, operadora {}, no periodo de {} ate {}", integracao.getEmpresa().getRazaoSocial(), integracao.getOperadora().getDescricao(), periodo, periodo);
        List<Venda> vendas = integracaoIfoodService.pesquisarVendas(integracao.getCodigoIntegracao(), periodo.getDe(), periodo.getAte());
        salvarVendas(vendas);
        return vendas;
    }

    private void importarCancelamentos(Integracao integracao, PeriodoDTO periodo) {
        log.info("Pesquisando cancelamentos no periodo de {} ate {}", periodo.getDe(), periodo.getAte());
        List<Cancelamento> cancelamentos = integracaoIfoodService.pesquisarCancelamentos(integracao.getCodigoIntegracao(), periodo.getDe(), periodo.getAte());
        salvarCancelamentos(cancelamentos);
    }

    private void importarAjusteVendas(Integracao integracao, PeriodoDTO periodo) {
        log.info("Pesquisando ajuste de vendas para empresa {}, operadora {}, no periodo de {} ate {}", integracao.getEmpresa().getRazaoSocial(), integracao.getOperadora().getDescricao(), periodo.getDe().withDayOfMonth(1), periodo.getAte());
        List<AjusteVenda> ajusteVendas = integracaoIfoodService.pesquisarAjusteVendas(integracao.getCodigoIntegracao(), periodo.getDe().withDayOfMonth(1), periodo.getAte());
        salvarAjusteVendas(ajusteVendas);
    }

    private void importarOcorrencias(Integracao integracao, PeriodoDTO periodo) {
        log.info("Pesquisando ocorrencias para empresa {}, operadora {}, no periodo de {} ate {}", integracao.getEmpresa().getRazaoSocial(), integracao.getOperadora().getDescricao(), periodo.getDe(), periodo.getAte());
        List<Ocorrencia> ocorrencias = integracaoIfoodService.pesquisarOcorrencias(integracao.getCodigoIntegracao(), periodo.getDe(), periodo.getAte());
        salvarOcorrenciaDeVendas(ocorrencias);
    }

    @Override
    public TipoImportacao tipoImportacao() {
        return TipoImportacao.IFOOD;
    }
}

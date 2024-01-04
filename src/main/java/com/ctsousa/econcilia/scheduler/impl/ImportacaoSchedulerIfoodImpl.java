package com.ctsousa.econcilia.scheduler.impl;

import com.ctsousa.econcilia.enumaration.ImportacaoSituacao;
import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.model.Importacao;
import com.ctsousa.econcilia.model.Integracao;
import com.ctsousa.econcilia.model.Venda;
import com.ctsousa.econcilia.repository.VendaRepository;
import com.ctsousa.econcilia.scheduler.ImportacaoAbstract;
import com.ctsousa.econcilia.scheduler.Scheduler;
import com.ctsousa.econcilia.scheduler.TipoImportacao;
import com.ctsousa.econcilia.service.ImportacaoService;
import com.ctsousa.econcilia.service.IntegracaoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ImportacaoSchedulerIfoodImpl extends ImportacaoAbstract implements Scheduler {

    private static final long QUINZE_MINUTOS = 900000L;

    private final ImportacaoService importacaoService;

    private final IntegracaoService integracaoService;

    private final VendaRepository vendaRepository;

    public ImportacaoSchedulerIfoodImpl(ImportacaoService importacaoService, IntegracaoService integracaoService, VendaRepository vendaRepository) {
        super(importacaoService);
        this.importacaoService = importacaoService;
        this.integracaoService = integracaoService;
        this.vendaRepository = vendaRepository;
    }

    @Override
    @Scheduled(fixedRate = QUINZE_MINUTOS)
    public void processar() {
        List<Periodo> periodos = new ArrayList<>();

        if (importacao != null) {
            log.info("Iniciando processamento para empresa {}, operadora {}, periodo inicial {}, periodo final {} ", importacao.getEmpresa().getRazaoSocial(), importacao.getOperadora().getDescricao(), importacao.getDataInicial(), importacao.getDataFinal());

            long totalDias = ChronoUnit.DAYS.between(importacao.getDataInicial(), importacao.getDataFinal());
            if (totalDias == 0) totalDias++;

            periodos = calcularPeriodo(importacao.getDataInicial(), totalDias);
        }

        if (periodos.isEmpty()) {
            log.info("Nenhum periodo encontrado para ser importado.");
            return;
        }

        String codigoIntegeracao = getCodigoIntegracao(importacao);
        importacaoService.atualizaPara(importacao, ImportacaoSituacao.EM_PROCESSAMENTO);

        for (Periodo periodo : periodos) {
            log.info("Pesquisando as vendas para empresa {}, operadora {}, no periodo de {} ate {}", importacao.getEmpresa().getRazaoSocial(), importacao.getOperadora().getDescricao(), periodo.getDe(), periodo.getAte());
            List<Venda> vendas = integracaoService.pesquisarVendasIfood(codigoIntegeracao, null, null, null, periodo.getDe(), periodo.getAte());

            if (vendas.isEmpty()) continue;

            log.info("Quantidade de vendas encontradas {}", vendas.size());
            log.info("Importando vendas na base de dados....");

            for (Venda venda : vendas) {
                venda.setEmpresa(importacao.getEmpresa());
                venda.setOperadora(importacao.getOperadora());

                vendaRepository.save(venda);
            }
        }

        log.info("Atualizando situação importação ...");
        importacaoService.atualizaPara(importacao, ImportacaoSituacao.PROCESSADO);

        log.info("Importação concluída com sucesso.");
    }

    private String getCodigoIntegracao(final Importacao importacao) {
        var empresa = importacao.getEmpresa();
        var operadora = importacao.getOperadora();

        List<Integracao> integracoes = integracaoService.pesquisar(empresa.getId(), operadora.getId(), null);

        if (integracoes.isEmpty()) {
            throw new NotificacaoException(String.format("Nenhum codigo integracao encontrado para empresa %s e operadora %s", empresa.getRazaoSocial(), operadora.getDescricao()));
        }

        return integracoes.get(0).getCodigoIntegracao();
    }

    @Override
    public TipoImportacao tipoImportacao() {
        return TipoImportacao.IFOOD;
    }
}

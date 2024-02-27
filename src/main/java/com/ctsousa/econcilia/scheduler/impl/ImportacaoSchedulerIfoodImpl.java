package com.ctsousa.econcilia.scheduler.impl;

import com.ctsousa.econcilia.enumaration.ImportacaoSituacao;
import com.ctsousa.econcilia.model.Venda;
import com.ctsousa.econcilia.model.dto.PeriodoDTO;
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
public class ImportacaoSchedulerIfoodImpl extends ImportacaoAbstract implements Scheduler {

    private static final long QUINZE_MINUTOS = 900000L;

    private final ImportacaoService importacaoService;

    private final IntegracaoIfoodService integracaoIfoodService;

    private final VendaRepository vendaRepository;

    @Setter
    @Value("${importacao_habilitar}")
    private boolean habilitar;

    public ImportacaoSchedulerIfoodImpl(ImportacaoService importacaoService, IntegracaoService integracaoService, VendaRepository vendaRepository, IntegracaoIfoodService integracaoIfoodService) {
        super(importacaoService, integracaoService);
        this.importacaoService = importacaoService;
        this.vendaRepository = vendaRepository;
        this.integracaoIfoodService = integracaoIfoodService;
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

        String codigoIntegeracao = getCodigoIntegracao();
        importacaoService.atualizaPara(importacao, ImportacaoSituacao.EM_PROCESSAMENTO);
        importar(periodos, codigoIntegeracao);

        log.info("Importação concluída com sucesso.");
    }

    private void importar(final List<PeriodoDTO> periodos, final String codigoIntegeracao) {
        for (PeriodoDTO periodo : periodos) {
            log.info("Pesquisando as vendas para empresa {}, operadora {}, no periodo de {} ate {}", importacao.getEmpresa().getRazaoSocial(), importacao.getOperadora().getDescricao(), periodo.getDe(), periodo.getAte());
            List<Venda> vendas = integracaoIfoodService.pesquisarVendas(codigoIntegeracao, null, null, null, periodo.getDe(), periodo.getAte());

            if (vendas.isEmpty()) continue;

            log.info("Quantidade de vendas encontradas {}", vendas.size());
            log.info("Importando vendas na base de dados....");
            salvarVendas(vendas);
        }
        log.info("Atualizando situação da importação ...");
        importacaoService.atualizaPara(importacao, ImportacaoSituacao.PROCESSADO);
    }

    private void salvarVendas(final List<Venda> vendas) {
        for (Venda venda : vendas) {
            venda.setEmpresa(importacao.getEmpresa());
            venda.setOperadora(importacao.getOperadora());
            vendaRepository.save(venda);
        }
    }

    @Override
    public TipoImportacao tipoImportacao() {
        return TipoImportacao.IFOOD;
    }
}

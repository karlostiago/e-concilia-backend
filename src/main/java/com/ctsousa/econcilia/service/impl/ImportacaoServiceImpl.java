package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.enumaration.ImportacaoSituacao;
import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.exceptions.Severidade;
import com.ctsousa.econcilia.model.Importacao;
import com.ctsousa.econcilia.repository.ImportacaoRepository;
import com.ctsousa.econcilia.scheduler.Scheduler;
import com.ctsousa.econcilia.scheduler.impl.ImportacaoProgramadaSchedulerIfoodImpl;
import com.ctsousa.econcilia.service.ImportacaoService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ImportacaoServiceImpl implements ImportacaoService {

    private final ImportacaoRepository importacaoRepository;

    public ImportacaoServiceImpl(ImportacaoRepository importacaoRepository) {
        this.importacaoRepository = importacaoRepository;
    }

    @Override
    public Importacao agendar(final Importacao importacao) {
        if (importacao.getDataInicial().isAfter(LocalDate.now().minusDays(1)) || importacao.getDataFinal().isAfter(LocalDate.now())) {
            throw new NotificacaoException("Não pode ser realizado um agendamento com data futura.");
        }

        if (importacao.getOperadora() == null || importacao.getOperadora().getId() == null) {
            throw new NotificacaoException("Selecione uma operadora para finalizar o agendamento.");
        }

        if (importacao.getEmpresa() == null || importacao.getEmpresa().getId() == null) {
            throw new NotificacaoException("Selecione uma empresa para finalizar o agendamento.");
        }

        if (temImportacao(importacao)) {
            throw new NotificacaoException("Já existe uma empresa com operadora selecionada com agendamento programado. Aguarde a execução do agendamento para realizar um novo agendamento para está empresa e operadora.");
        }

        return this.importacaoRepository.save(importacao);
    }

    @Override
    public List<Importacao> buscarImportacoes() {
        return this.importacaoRepository.pesquisarImportacoes();
    }

    @Override
    public void temImportacaoProgramada() {
        Boolean temImportacaoProgramada = importacaoRepository.existsPorSituacao(List.of(ImportacaoSituacao.ERRO_PROCESSAMENTO, ImportacaoSituacao.AGENDADO));
        if (!temImportacaoProgramada) {
            throw new NotificacaoException("Não existe importação para ser processada.", Severidade.ATENCAO);
        }
    }

    @Override
    public void atualizaPara(Importacao importacao, ImportacaoSituacao situacao) {
        importacao.setSituacao(situacao);
        this.importacaoRepository.save(importacao);
    }

    private boolean temImportacao(final Importacao importacao) {
        var importacoes = buscarImportacoes()
                .stream().filter(i -> i.getSituacao().equals(ImportacaoSituacao.AGENDADO))
                .toList();

        for (Importacao imp : importacoes) {
            if (imp.getOperadora().getId().equals(importacao.getOperadora().getId())
                    && imp.getEmpresa().getId().equals(importacao.getEmpresa().getId())) {
                return true;
            }
        }

        return false;
    }
}

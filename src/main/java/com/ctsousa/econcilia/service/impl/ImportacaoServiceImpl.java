package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.enumaration.ImportacaoSituacao;
import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.model.Importacao;
import com.ctsousa.econcilia.repository.ImportacaoRepository;
import com.ctsousa.econcilia.service.ImportacaoService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ImportacaoServiceImpl implements ImportacaoService {

    private final ImportacaoRepository importacaoRepository;

    public ImportacaoServiceImpl(ImportacaoRepository importacaoRepository) {
        this.importacaoRepository = importacaoRepository;
    }

    @Override
    public Importacao agendar(final Importacao importacao) {
        if (temImportacaoAgendada(importacao)) {
            throw new NotificacaoException("Já existe uma empresa com operadora selecionada com agendamento programado. Aguarde a execução do agendamento para realizar um novo agendamento para está empresa e operadora.");
        }
        return this.importacaoRepository.save(importacao);
    }

    @Override
    public List<Importacao> buscarPorSituacaoAgendada() {
        return this.importacaoRepository.buscarPorSituacaoAgendada(ImportacaoSituacao.AGENDADA);
    }

    private boolean temImportacaoAgendada(final Importacao importacao) {
        var importacoes = buscarPorSituacaoAgendada();

        for (Importacao imp : importacoes) {
            if (imp.getOperadora().getId().equals(importacao.getOperadora().getId())
                    && imp.getEmpresa().getId().equals(importacao.getEmpresa().getId())) {
                    return true;
            }
        }

        return false;
    }
}

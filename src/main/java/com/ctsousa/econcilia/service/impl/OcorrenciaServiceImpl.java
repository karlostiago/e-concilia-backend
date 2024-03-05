package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Integracao;
import com.ctsousa.econcilia.model.Ocorrencia;
import com.ctsousa.econcilia.model.Operadora;
import com.ctsousa.econcilia.repository.OcorrenciaRepository;
import com.ctsousa.econcilia.service.IntegracaoIfoodService;
import com.ctsousa.econcilia.service.OcorrenciaService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class OcorrenciaServiceImpl implements OcorrenciaService {

    private final IntegracaoIfoodService integracaoIfoodService;

    private final OcorrenciaRepository ocorrenciaRepository;

    public OcorrenciaServiceImpl(IntegracaoIfoodService integracaoIfoodService, OcorrenciaRepository ocorrenciaRepository) {
        this.integracaoIfoodService = integracaoIfoodService;
        this.ocorrenciaRepository = ocorrenciaRepository;
    }

    @Override
    public List<Ocorrencia> buscar(final Integracao integracao, final LocalDate dataInicial, final LocalDate dataFinal) {

        if (integracao == null) {
            throw new NotificacaoException("Nenhuma integração informada.");
        }

        if (dataInicial == null || dataFinal == null) {
            throw new NotificacaoException("O período inicial e final deve ser informado.");
        }

        Empresa empresa = integracao.getEmpresa();
        Operadora operadora = integracao.getOperadora();
        List<Ocorrencia> ocorrencias = ocorrenciaRepository.buscar(dataInicial, dataFinal, empresa.getId(), operadora.getId());

        if (ocorrencias.isEmpty()) {
            String codigo = integracao.getCodigoIntegracao();
            ocorrencias = integracaoIfoodService.pesquisarOcorrencias(codigo, dataInicial, dataFinal);
        }

        return ocorrencias;
    }
}

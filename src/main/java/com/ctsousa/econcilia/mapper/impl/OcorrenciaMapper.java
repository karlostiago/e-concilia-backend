package com.ctsousa.econcilia.mapper.impl;

import com.ctsousa.econcilia.annotation.ExcludedCoverage;
import com.ctsousa.econcilia.integration.ifood.entity.Occurrence;
import com.ctsousa.econcilia.mapper.ColecaoMapper;
import com.ctsousa.econcilia.mapper.DtoMapper;
import com.ctsousa.econcilia.model.Ocorrencia;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ExcludedCoverage
public class OcorrenciaMapper implements DtoMapper<Occurrence, Ocorrencia>, ColecaoMapper<Occurrence, Ocorrencia> {

    @Override
    public List<Ocorrencia> paraLista(List<Occurrence> occurrences) {
        return occurrences.stream()
                .map(this::paraDTO)
                .toList();
    }

    @Override
    public Ocorrencia paraDTO(Occurrence occurrence) {
        Ocorrencia ocorrencia = new Ocorrencia();
        ocorrencia.setPeriodoId(occurrence.getPeriodId() != null ? occurrence.getPeriodId() : "");
        ocorrencia.setValor(occurrence.getAmount());
        ocorrencia.setTransacaoId(occurrence.getTransactionId());
        ocorrencia.setDataTransacao(occurrence.getTransactionDate());
        ocorrencia.setExpectativaDataPagamento(occurrence.getExpectedPaymentDate());
        return ocorrencia;
    }
}

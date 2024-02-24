package com.ctsousa.econcilia.mapper.impl;

import com.ctsousa.econcilia.annotation.ExcludedCoverage;
import com.ctsousa.econcilia.integration.ifood.entity.IncomeTaxe;
import com.ctsousa.econcilia.mapper.ColecaoMapper;
import com.ctsousa.econcilia.mapper.DtoMapper;
import com.ctsousa.econcilia.model.ImpostoRenda;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ExcludedCoverage
public class ImpostoRendaMapper implements DtoMapper<IncomeTaxe, ImpostoRenda>, ColecaoMapper<IncomeTaxe, ImpostoRenda> {

    @Override
    public List<ImpostoRenda> paraLista(List<IncomeTaxe> incomeTaxes) {
        return incomeTaxes.stream()
                .map(this::paraDTO)
                .toList();
    }

    @Override
    public ImpostoRenda paraDTO(IncomeTaxe incomeTaxe) {
        ImpostoRenda impostoRenda = new ImpostoRenda();
        impostoRenda.setPeriodoId(incomeTaxe.getPeriodId());
        impostoRenda.setValor(incomeTaxe.getAmount());
        impostoRenda.setDataTransacao(incomeTaxe.getTransactionDate());
        impostoRenda.setTransacaoId(incomeTaxe.getTransactionId());
        impostoRenda.setExpectativaDataPagamento(incomeTaxe.getExpectedPaymentDate());
        return impostoRenda;
    }
}

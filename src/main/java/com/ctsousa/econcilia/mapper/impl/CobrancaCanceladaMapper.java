package com.ctsousa.econcilia.mapper.impl;

import com.ctsousa.econcilia.integration.ifood.entity.ChargeCancellation;
import com.ctsousa.econcilia.mapper.ColecaoMapper;
import com.ctsousa.econcilia.mapper.DtoMapper;
import com.ctsousa.econcilia.model.CobrancaCancelada;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CobrancaCanceladaMapper implements DtoMapper<ChargeCancellation, CobrancaCancelada>, ColecaoMapper<ChargeCancellation, CobrancaCancelada> {

    @Override
    public List<CobrancaCancelada> paraLista(List<ChargeCancellation> chargeCancellations) {
        return chargeCancellations.stream()
                .map(this::paraDTO)
                .toList();
    }

    @Override
    public CobrancaCancelada paraDTO(ChargeCancellation chargeCancellation) {
        CobrancaCancelada cobrancaCancelada = new CobrancaCancelada();
        cobrancaCancelada.setDataTransacao(chargeCancellation.getTransactionDate());
        cobrancaCancelada.setValor(chargeCancellation.getAmount());
        cobrancaCancelada.setExpectativaDataPagamento(chargeCancellation.getExpectedPaymentDate());
        cobrancaCancelada.setPlanoPagamento(chargeCancellation.getPaymentPlan());
        cobrancaCancelada.setNomeComerciante(chargeCancellation.getMerchantName());
        cobrancaCancelada.setTransacaoId(chargeCancellation.getTransactionId());
        cobrancaCancelada.setPeriodoId(cobrancaCancelada.getPeriodoId());
        return cobrancaCancelada;
    }
}

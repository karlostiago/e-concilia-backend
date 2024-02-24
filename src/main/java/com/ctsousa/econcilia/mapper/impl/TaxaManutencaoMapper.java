package com.ctsousa.econcilia.mapper.impl;

import com.ctsousa.econcilia.annotation.ExcludedCoverage;
import com.ctsousa.econcilia.integration.ifood.entity.MaintenanceFee;
import com.ctsousa.econcilia.mapper.ColecaoMapper;
import com.ctsousa.econcilia.mapper.DtoMapper;
import com.ctsousa.econcilia.model.TaxaManutencao;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ExcludedCoverage
public class TaxaManutencaoMapper implements DtoMapper<MaintenanceFee, TaxaManutencao>, ColecaoMapper<MaintenanceFee, TaxaManutencao> {

    @Override
    public List<TaxaManutencao> paraLista(List<MaintenanceFee> maintenanceFees) {
        return maintenanceFees.stream()
                .map(this::paraDTO)
                .toList();
    }

    @Override
    public TaxaManutencao paraDTO(MaintenanceFee maintenanceFee) {
        TaxaManutencao taxaManutencao = new TaxaManutencao();
        taxaManutencao.setPeriodoId(maintenanceFee.getPeriodId());
        taxaManutencao.setValor(maintenanceFee.getAmount());
        taxaManutencao.setDataTransacao(maintenanceFee.getTransactionDate());
        taxaManutencao.setTransacaoId(maintenanceFee.getTransactionId());
        taxaManutencao.setExpectativaDataPagamento(maintenanceFee.getExpectedPaymentDate());
        return taxaManutencao;
    }
}

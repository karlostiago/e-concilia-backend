package com.ctsousa.econcilia.mapper.impl;

import com.ctsousa.econcilia.Mapper;
import com.ctsousa.econcilia.integration.ifood.entity.Cancellation;
import com.ctsousa.econcilia.mapper.ColecaoMapper;
import com.ctsousa.econcilia.mapper.DtoMapper;
import com.ctsousa.econcilia.model.Cancelamento;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public class CancelamentoMapper implements DtoMapper<Cancellation, Cancelamento>, ColecaoMapper<Cancellation, Cancelamento> {

    @Override
    public List<Cancelamento> paraLista(List<Cancellation> cancellations) {
        return cancellations.stream()
                .map(this::paraDTO)
                .toList();
    }

    @Override
    public Cancelamento paraDTO(Cancellation cancellation) {
        Cancelamento cancelamento = new Cancelamento();
        cancelamento.setComercianteId(cancellation.getMerchantId());
        cancelamento.setNomeComerciante(cancellation.getMerchantName());
        cancelamento.setValor(cancellation.getAmount());
        cancelamento.setPeriodoId(cancellation.getPeriodId());
        cancelamento.setPedidoId(cancellation.getOrderId());
        return cancelamento;
    }
}

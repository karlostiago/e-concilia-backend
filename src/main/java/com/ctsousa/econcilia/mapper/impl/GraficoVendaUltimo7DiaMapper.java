package com.ctsousa.econcilia.mapper.impl;

import com.ctsousa.econcilia.model.Venda;
import com.ctsousa.econcilia.model.dto.GraficoVendaUltimo7DiaDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class GraficoVendaUltimo7DiaMapper {

    public GraficoVendaUltimo7DiaDTO toDTO (final List<Venda> vendas) {
        GraficoVendaUltimo7DiaDTO dto = new GraficoVendaUltimo7DiaDTO();
        dto.setData(new ArrayList<>(vendas.size()));
        dto.setLabels(new ArrayList<>(vendas.size()));

        Map<LocalDate, List<Venda>> vendasMap = vendas.stream()
                .collect(Collectors.groupingBy(Venda::getDataPedido));

        for (Map.Entry<LocalDate, List<Venda>> entry : vendasMap.entrySet()) {
            var total = somarVendas(entry.getValue());
            dto.getLabels().add(formatarDataVenda(entry.getKey()));
            dto.getData().add(total);
        }

        return dto;
    }

    private String formatarDataVenda(final LocalDate dataVenda) {
        return dataVenda.getDayOfMonth() + "/" + dataVenda.getMonth().getValue();
    }

    private BigDecimal somarVendas(final List<Venda> vendas) {
        return vendas.stream()
                .map(venda -> venda.getCobranca().getValorBruto())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

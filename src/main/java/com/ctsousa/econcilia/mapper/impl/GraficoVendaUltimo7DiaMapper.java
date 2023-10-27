package com.ctsousa.econcilia.mapper.impl;

import com.ctsousa.econcilia.model.Venda;
import com.ctsousa.econcilia.model.dto.GraficoVendaUltimo7DiaDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class GraficoVendaUltimo7DiaMapper {

    public GraficoVendaUltimo7DiaDTO toDTO (final List<Venda> vendas) {
        GraficoVendaUltimo7DiaDTO dto = new GraficoVendaUltimo7DiaDTO();
        dto.setData(new ArrayList<>(vendas.size()));
        dto.setLabels(new ArrayList<>(vendas.size()));

        Map<LocalDate, BigDecimal> ultimas7DiasMap = ultimos7Dias();
        ultimas7DiasMap = ordenacaoCrescente(ultimas7DiasMap);

        Map<LocalDate, List<Venda>> vendasMap = vendas.stream()
                .collect(Collectors.groupingBy(Venda::getDataPedido));

        for (Map.Entry<LocalDate, List<Venda>> entry : vendasMap.entrySet()) {
            var total = somarVendas(entry.getValue());
            ultimas7DiasMap.put(entry.getKey(), total);
        }

        for (Map.Entry<LocalDate, BigDecimal> entry : ultimas7DiasMap.entrySet()) {
            dto.getLabels().add(formatarDataVenda(entry.getKey()));
            dto.getData().add(entry.getValue());
        }

        return dto;
    }

    private Map<LocalDate, BigDecimal> ultimos7Dias() {
        var diaAnterior = LocalDate.now().minusDays(1);
        return IntStream.range(0, 7)
                .mapToObj(diaAnterior::minusDays)
                .collect(Collectors.toMap(data -> data, data -> BigDecimal.ZERO));
    }

    private Map<LocalDate, BigDecimal> ordenacaoCrescente(Map<LocalDate, BigDecimal> ultimas7DiasMap) {
        return ultimas7DiasMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (valorAtual, novoValor) -> valorAtual, LinkedHashMap::new));

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

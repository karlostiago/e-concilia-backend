package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.model.Venda;
import com.ctsousa.econcilia.model.dto.ConsolidadoDTO;
import com.ctsousa.econcilia.model.dto.GraficoVendaUltimo7DiaDTO;
import com.ctsousa.econcilia.report.dto.RelatorioConsolidadoDTO;
import com.ctsousa.econcilia.service.GraficoVendaService;
import com.ctsousa.econcilia.util.DataUtil;
import com.ctsousa.econcilia.util.DecimalUtil;
import com.ctsousa.econcilia.util.StringUtil;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.ctsousa.econcilia.util.CalculadoraUtil.somar;
import static com.ctsousa.econcilia.util.DataUtil.diaMes;
import static com.ctsousa.econcilia.util.DataUtil.paraLocalDate;
import static com.ctsousa.econcilia.util.DecimalUtil.paraDecimal;

@Component
public class GraficoVendaUltimo7DiaServiceImpl implements GraficoVendaService<GraficoVendaUltimo7DiaDTO> {

    @Override
    public GraficoVendaUltimo7DiaDTO processar(List<Venda> vendas) {
        LocalDate periodo = vendas.get(vendas.size() - 1).getDataPedido();

        GraficoVendaUltimo7DiaDTO dto = new GraficoVendaUltimo7DiaDTO();
        dto.setData(new ArrayList<>(vendas.size()));
        dto.setLabels(new ArrayList<>(vendas.size()));

        Map<LocalDate, BigDecimal> ultimas7DiasMap = ultimos7Dias(periodo);
        ultimas7DiasMap = ordenacaoCrescente(ultimas7DiasMap);

        Map<LocalDate, List<Venda>> vendasMap = vendas.stream()
                .collect(Collectors.groupingBy(Venda::getDataPedido));

        for (Map.Entry<LocalDate, List<Venda>> entry : vendasMap.entrySet()) {
            var total = somarVendas(entry.getValue());
            if (entry.getKey().isBefore(LocalDate.now()) && ultimas7DiasMap.containsKey(entry.getKey())) {
                ultimas7DiasMap.put(entry.getKey(), total);
            }
        }

        for (Map.Entry<LocalDate, BigDecimal> entry : ultimas7DiasMap.entrySet()) {
            dto.getLabels().add(formatarDataVenda(entry.getKey()));
            dto.getData().add(entry.getValue());
        }

        return dto;
    }

    public GraficoVendaUltimo7DiaDTO processar(LocalDate periodo, List<RelatorioConsolidadoDTO> consolidados) {
        GraficoVendaUltimo7DiaDTO dto = new GraficoVendaUltimo7DiaDTO();
        dto.setData(new ArrayList<>());
        dto.setLabels(new ArrayList<>());

        Map<LocalDate, BigDecimal> ultimas7DiasMap = ultimos7Dias(periodo.minusDays(1));
        ultimas7DiasMap = ordenacaoCrescente(ultimas7DiasMap);

        for (RelatorioConsolidadoDTO consolidadoDTO : consolidados) {
            if (StringUtil.naoTemValor(consolidadoDTO.getPeriodo())) continue;

            LocalDate dataVenda = paraLocalDate(consolidadoDTO.getPeriodo());
            if (ultimas7DiasMap.containsKey(dataVenda)) {
                ultimas7DiasMap.put(dataVenda, paraDecimal(consolidadoDTO.getTotalBruto()));
            }
        }

        for (Map.Entry<LocalDate, BigDecimal> entry : ultimas7DiasMap.entrySet()) {
            dto.getLabels().add(formatarDataVenda(entry.getKey()));
            dto.getData().add(entry.getValue());
        }

        return dto;
    }

    private Map<LocalDate, BigDecimal> ultimos7Dias(LocalDate periodo) {
        var diaAnterior = periodo.minusDays(0);
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
        return diaMes(dataVenda);
    }

    private BigDecimal somarVendas(final List<Venda> vendas) {

        List<BigDecimal> valores = vendas.stream()
                .map(venda -> venda.getCobranca().getValorBruto())
                .toList();

        return somar(valores);
    }
}

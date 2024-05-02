package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.model.Venda;
import com.ctsousa.econcilia.model.dto.GraficoVendaUltimo7DiaDinheiroPixDTO;
import com.ctsousa.econcilia.report.dto.RelatorioConsolidadoDTO;
import com.ctsousa.econcilia.service.AbstractGraficoVendaMeioPagamento;
import com.ctsousa.econcilia.service.GraficoVendaService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class GraficoVendaUltimo7DiaDinheiroPixServiceImpl extends AbstractGraficoVendaMeioPagamento implements GraficoVendaService<GraficoVendaUltimo7DiaDinheiroPixDTO> {

    @Override
    public GraficoVendaUltimo7DiaDinheiroPixDTO processar(List<Venda> vendas) {
        LocalDate periodo = vendas.get(vendas.size() - 1).getDataPedido();

        GraficoVendaUltimo7DiaDinheiroPixDTO graficoDTO = new GraficoVendaUltimo7DiaDinheiroPixDTO();

        graficoDTO.setLabels(new ArrayList<>());
        graficoDTO.setDataCash(new ArrayList<>());
        graficoDTO.setDataPix(new ArrayList<>());

        Map<LocalDate, Map<String, BigDecimal>> ultimas7DiasMap = ultimos7Dias(periodo);
        ultimas7DiasMap = ordenacaoCrescente(ultimas7DiasMap);

        Map<LocalDate, List<Venda>> vendasMap = vendas.stream()
                .collect(Collectors.groupingBy(Venda::getDataPedido));

        adicionarVendasTotalizadas(ultimas7DiasMap, vendasMap);

        for (Map.Entry<LocalDate, Map<String, BigDecimal>> entry : ultimas7DiasMap.entrySet()) {
            graficoDTO.getLabels().add(formatarDataVenda(entry.getKey()));
            graficoDTO.getDataPix().add(entry.getValue().get(PIX));
            graficoDTO.getDataCash().add(entry.getValue().get(DINHEIRO));
        }

        return graficoDTO;
    }

    @Override
    public GraficoVendaUltimo7DiaDinheiroPixDTO processar(LocalDate periodo, List<RelatorioConsolidadoDTO> consolidados) {
        return null;
    }
}

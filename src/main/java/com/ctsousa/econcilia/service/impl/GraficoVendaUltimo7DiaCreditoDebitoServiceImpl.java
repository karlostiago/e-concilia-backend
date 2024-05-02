package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.model.Venda;
import com.ctsousa.econcilia.model.dto.GraficoVendaUltimo7DiaCreditoDebitoDTO;
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
public class GraficoVendaUltimo7DiaCreditoDebitoServiceImpl extends AbstractGraficoVendaMeioPagamento implements GraficoVendaService<GraficoVendaUltimo7DiaCreditoDebitoDTO> {

    @Override
    public GraficoVendaUltimo7DiaCreditoDebitoDTO processar(List<Venda> vendas) {
        LocalDate periodo = vendas.get(vendas.size() - 1).getDataPedido();

        GraficoVendaUltimo7DiaCreditoDebitoDTO graficoDTO = new GraficoVendaUltimo7DiaCreditoDebitoDTO();

        graficoDTO.setLabels(new ArrayList<>());
        graficoDTO.setDataCredit(new ArrayList<>());
        graficoDTO.setDataDebit(new ArrayList<>());

        Map<LocalDate, Map<String, BigDecimal>> ultimas7DiasMap = ultimos7Dias(periodo);
        ultimas7DiasMap = ordenacaoCrescente(ultimas7DiasMap);

        Map<LocalDate, List<Venda>> vendasMap = vendas.stream()
                .collect(Collectors.groupingBy(Venda::getDataPedido));

        adicionarVendasTotalizadas(ultimas7DiasMap, vendasMap);

        for (Map.Entry<LocalDate, Map<String, BigDecimal>> entry : ultimas7DiasMap.entrySet()) {
            graficoDTO.getLabels().add(formatarDataVenda(entry.getKey()));
            graficoDTO.getDataDebit().add(entry.getValue().get(DEBITO));
            graficoDTO.getDataCredit().add(entry.getValue().get(CREDITO));
        }

        return graficoDTO;
    }

    @Override
    public GraficoVendaUltimo7DiaCreditoDebitoDTO processar(LocalDate periodo, List<RelatorioConsolidadoDTO> consolidados) {
        return null;
    }
}

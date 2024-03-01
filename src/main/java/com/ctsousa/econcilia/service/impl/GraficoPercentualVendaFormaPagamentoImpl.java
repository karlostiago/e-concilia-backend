package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.model.Venda;
import com.ctsousa.econcilia.model.dto.GraficoPercentualVendaFormaPagamentoDTO;
import com.ctsousa.econcilia.service.AbstractGraficoVendaMeioPagamento;
import com.ctsousa.econcilia.service.GraficoVendaService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Component
public class GraficoPercentualVendaFormaPagamentoImpl extends AbstractGraficoVendaMeioPagamento implements GraficoVendaService<GraficoPercentualVendaFormaPagamentoDTO>  {
    @Override
    public GraficoPercentualVendaFormaPagamentoDTO processar(List<Venda> vendas) {

        GraficoPercentualVendaFormaPagamentoDTO graficoDTO = new GraficoPercentualVendaFormaPagamentoDTO();
        graficoDTO.setLabels(List.of("Crédito", "Débito", "Dinheiro", "Pix", "Outros"));

        Map<LocalDate, List<Venda>> vendasMap = vendas.stream()
                .collect(Collectors.groupingBy(Venda::getDataPedido));

        var totalizadorMap = totalizarVendasPorMeioPagamento(vendasMap);

        var percentualPix = calcular(totalizadorMap, PIX);
        var percentualCredito = calcular(totalizadorMap, CREDITO);
        var percentualDebito = calcular(totalizadorMap, DEBITO);
        var percentualDinheiro = calcular(totalizadorMap, DINHEIRO);
        var percentualOutros = calcular(totalizadorMap, OUTROS);

        graficoDTO.setData(List.of(percentualCredito, percentualDebito, percentualDinheiro, percentualPix, percentualOutros));

        return graficoDTO;
    }

    private BigDecimal calcular(Map<LocalDate, Map<String, BigDecimal>> totalizadorMap, String tipoPagamento) {
        AtomicReference<BigDecimal> valorTotal = new AtomicReference<>(BigDecimal.ZERO);
        BigDecimal percentual = BigDecimal.ZERO;

        totalizadorMap.forEach((localDate, valor) -> valorTotal.updateAndGet(currentValue -> currentValue.add(valor.get(tipoPagamento))));

        if (valorTotal.get().compareTo(BigDecimal.ZERO) > 0) {
            percentual = valorTotal.get().divide(getValorTotal(), RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100D));
        }

        return percentual;
    }
}

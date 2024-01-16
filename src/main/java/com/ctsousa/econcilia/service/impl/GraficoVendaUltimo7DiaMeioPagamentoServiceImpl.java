package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.model.Venda;
import com.ctsousa.econcilia.model.dto.GraficoVendaUltimo7DiaMeioPagamentoDTO;
import com.ctsousa.econcilia.service.GraficoVendaService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.ctsousa.econcilia.util.DataUtil.diaMes;

@Component
public class GraficoVendaUltimo7DiaMeioPagamentoServiceImpl implements GraficoVendaService<GraficoVendaUltimo7DiaMeioPagamentoDTO> {

    private static final String DINHEIRO = "DINHEIRO";

    private static final String DEBITO = "DEBITO";

    private static final String CREDITO = "CREDITO";

    private static final String PIX = "PIX";

    private static final String OUTROS = "OUTROS";

    @Override
    public GraficoVendaUltimo7DiaMeioPagamentoDTO processar(List<Venda> vendas) {
        GraficoVendaUltimo7DiaMeioPagamentoDTO graficoDTO = new GraficoVendaUltimo7DiaMeioPagamentoDTO();

        graficoDTO.setLabels(new ArrayList<>());
        graficoDTO.setDataCredit(new ArrayList<>());
        graficoDTO.setDataCash(new ArrayList<>());
        graficoDTO.setDataPix(new ArrayList<>());
        graficoDTO.setDataDebit(new ArrayList<>());
        graficoDTO.setDataOther(new ArrayList<>());

        Map<LocalDate, Map<String, BigDecimal>> ultimas7DiasMap = ultimos7Dias();
        ultimas7DiasMap = ordenacaoCrescente(ultimas7DiasMap);

        Map<LocalDate, List<Venda>> vendasMap = vendas.stream()
                .collect(Collectors.groupingBy(Venda::getDataPedido));

        adicionarVendasTotalizadas(ultimas7DiasMap, vendasMap);

        for (Map.Entry<LocalDate, Map<String, BigDecimal>> entry : ultimas7DiasMap.entrySet()) {
            graficoDTO.getLabels().add(formatarDataVenda(entry.getKey()));
            graficoDTO.getDataDebit().add(entry.getValue().get(DEBITO));
            graficoDTO.getDataCredit().add(entry.getValue().get(CREDITO));
            graficoDTO.getDataPix().add(entry.getValue().get(PIX));
            graficoDTO.getDataCash().add(entry.getValue().get(DINHEIRO));
            graficoDTO.getDataOther().add(entry.getValue().get(OUTROS));
        }

        return graficoDTO;
    }

    private void adicionarVendasTotalizadas(Map<LocalDate, Map<String, BigDecimal>> ultimas7DiasMap, Map<LocalDate, List<Venda>> vendasMap) {
        var vendasTotalizadasMap = totalizarVendasPorMeioPagamento(vendasMap);

        for (Map.Entry<LocalDate, Map<String, BigDecimal>> entry : ultimas7DiasMap.entrySet()) {
            var valoresMap = vendasTotalizadasMap.get(entry.getKey());

            if (valoresMap == null) continue;

            Map<String, BigDecimal> totalizadorMap = new HashMap<>();

            totalizadorMap.put(PIX, valoresMap.get(PIX));
            totalizadorMap.put(DINHEIRO, valoresMap.get(DINHEIRO));
            totalizadorMap.put(DEBITO, valoresMap.get(DEBITO));
            totalizadorMap.put(CREDITO, valoresMap.get(CREDITO));
            totalizadorMap.put(OUTROS, valoresMap.get(OUTROS));

            ultimas7DiasMap.put(entry.getKey(), totalizadorMap);
        }
    }

    private Map<LocalDate, Map<String, BigDecimal>> totalizarVendasPorMeioPagamento(Map<LocalDate, List<Venda>> vendas) {
        Map<LocalDate, Map<String, BigDecimal>> totalizadorMap = new HashMap<>();

        for (Map.Entry<LocalDate, List<Venda>> entry : vendas.entrySet()) {
            Map<String, BigDecimal> totalizadorMeioPagamentoMap = calcularVendaPorMeioPagamento(entry.getValue());
            totalizadorMap.put(entry.getKey(), totalizadorMeioPagamentoMap);
        }

        return totalizadorMap;
    }

    private Map<String, BigDecimal> calcularVendaPorMeioPagamento(final List<Venda> vendas) {
        Map<String, BigDecimal> totalizadorMeioPagamentoMap = new HashMap<>();

        BigDecimal dinheiro = BigDecimal.ZERO;
        BigDecimal credito = BigDecimal.ZERO;
        BigDecimal debito = BigDecimal.ZERO;
        BigDecimal outros = BigDecimal.ZERO;
        BigDecimal pix = BigDecimal.ZERO;

        for (Venda venda : vendas) {
            if (venda.getPagamento().getMetodo().equalsIgnoreCase("debit")) {
                debito = debito.add(venda.getCobranca().getValorBruto());
            } else if (venda.getPagamento().getMetodo().equalsIgnoreCase("credit")) {
                credito = credito.add(venda.getCobranca().getValorBruto());
            } else if (venda.getPagamento().getMetodo().equalsIgnoreCase("pix")) {
                pix = pix.add(venda.getCobranca().getValorBruto());
            } else if (venda.getPagamento().getMetodo().equalsIgnoreCase("cash")) {
                dinheiro = dinheiro.add(venda.getCobranca().getValorBruto());
            } else {
                outros = outros.add(venda.getCobranca().getValorBruto());
            }
        }

        totalizadorMeioPagamentoMap.put(PIX, pix);
        totalizadorMeioPagamentoMap.put(DINHEIRO, dinheiro);
        totalizadorMeioPagamentoMap.put(DEBITO, debito);
        totalizadorMeioPagamentoMap.put(CREDITO, credito);
        totalizadorMeioPagamentoMap.put(OUTROS, outros);

        return totalizadorMeioPagamentoMap;
    }

    private Map<LocalDate, Map<String, BigDecimal>> ultimos7Dias() {
        var diaAnterior = LocalDate.now().minusDays(1);
        return IntStream.range(0, 7)
                .mapToObj(diaAnterior::minusDays)
                .collect(Collectors.toMap(data -> data, data -> new HashMap<>()));
    }

    private Map<LocalDate, Map<String, BigDecimal>> ordenacaoCrescente(Map<LocalDate, Map<String, BigDecimal>> ultimas7DiasMap) {
        return ultimas7DiasMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (valorAtual, novoValor) -> valorAtual, LinkedHashMap::new));

    }

    private String formatarDataVenda(final LocalDate dataVenda) {
        return diaMes(dataVenda);
    }
}

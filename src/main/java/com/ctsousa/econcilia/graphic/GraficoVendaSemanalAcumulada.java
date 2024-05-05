package com.ctsousa.econcilia.graphic;

import com.ctsousa.econcilia.model.dto.GraficoVendaUltimo7DiaDTO;
import com.ctsousa.econcilia.util.DataUtil;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.ctsousa.econcilia.util.DataUtil.diaMes;
import static com.ctsousa.econcilia.util.DataUtil.getUltimoDiaMes;

@Component
public class GraficoVendaSemanalAcumulada {

    public GraficoVendaUltimo7DiaDTO construir(Map<String, Map<LocalDate, BigDecimal>> vendasMap) {
        String chave = vendasMap.keySet().iterator().next();
        LocalDate periodo = (LocalDate) ((TreeMap<?, ?>)vendasMap.get(chave)).lastKey();

        if (DataUtil.isMesCorrente(periodo)) {
            periodo = LocalDate.now().minusDays(1);
        } else {
            periodo = getUltimoDiaMes(periodo);
        }

        Map<LocalDate, BigDecimal> ultimos7DiasMap = ultimos7Dias(periodo);
        ultimos7DiasMap = ordenacaoCrescente(ultimos7DiasMap);

        for (Map.Entry<String, Map<LocalDate, BigDecimal>> entry : vendasMap.entrySet()) {
            for (Map.Entry<LocalDate, BigDecimal> e : entry.getValue().entrySet()) {
                if (ultimos7DiasMap.containsKey(e.getKey())) {
                    BigDecimal totalBruto = ultimos7DiasMap.get(e.getKey());
                    totalBruto = totalBruto.add(e.getValue());
                    ultimos7DiasMap.put(e.getKey(), totalBruto);
                }
            }
        }

        GraficoVendaUltimo7DiaDTO graficoDTO = new GraficoVendaUltimo7DiaDTO();
        graficoDTO.setData(new ArrayList<>(ultimos7DiasMap.size()));
        graficoDTO.setLabels(new ArrayList<>(ultimos7DiasMap.size()));

        for (Map.Entry<LocalDate, BigDecimal> entry : ultimos7DiasMap.entrySet()) {
            graficoDTO.getLabels().add(formatarDataVenda(entry.getKey()));
            graficoDTO.getData().add(entry.getValue());
        }

        return graficoDTO;
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
}

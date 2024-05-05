package com.ctsousa.econcilia.graphic;

import com.ctsousa.econcilia.model.dto.DataSetDTO;
import com.ctsousa.econcilia.model.dto.GraficoVendaAnualDTO;
import com.ctsousa.econcilia.util.DataUtil;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class GraficoVendaAnual {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM/yyyy", Locale.forLanguageTag("pt-BR"));

    public GraficoVendaAnualDTO construir(Map<String, Map<YearMonth, BigDecimal>> mapVendas) {
        String chave = mapVendas.keySet().iterator().next();
        YearMonth periodo = (YearMonth) ((TreeMap<?, ?>)mapVendas.get(chave)).lastKey();

        GraficoVendaAnualDTO graficoVendaAnualDTO = new GraficoVendaAnualDTO();
        graficoVendaAnualDTO.setLabels(getLabels(periodo));

        graficoVendaAnualDTO.setDataSets(getDatasSet(mapVendas, graficoVendaAnualDTO.getLabels()));

        return graficoVendaAnualDTO;
    }

    private List<String> getLabels(final YearMonth periodoInicial) {
        YearMonth periodoFinal = periodoInicial.minusYears(1);
        List<String> labels = new ArrayList<>();

        while (!periodoFinal.isAfter(periodoInicial)) {
            String nomeMes = periodoFinal.format(formatter).replace(".", "");
            nomeMes = nomeMes.substring(0, 1).toUpperCase() + nomeMes.substring(1);
            labels.add(nomeMes);

            periodoFinal = periodoFinal.plusMonths(1);
        }
        return labels;
    }

    private List<DataSetDTO> getDatasSet(Map<String, Map<YearMonth, BigDecimal>> vendasAgrupadas, List<String> labels) {
        List<DataSetDTO> dataSets = new ArrayList<>();

        for (Map.Entry<String, Map<YearMonth, BigDecimal>> entry : vendasAgrupadas.entrySet()) {
            DataSetDTO dataSetDTO = new DataSetDTO();
            dataSetDTO.setLabel(entry.getKey());
            dataSetDTO.setData(new ArrayList<>());

            for (String label : labels) {
                YearMonth key = DataUtil.parseMesAno(label);
                dataSetDTO.getData().add( entry.getValue().get(key));
            }

            dataSets.add(dataSetDTO);
        }

        return dataSets;
    }
}

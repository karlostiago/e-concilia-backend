package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.model.Venda;
import com.ctsousa.econcilia.model.dto.DataSetDTO;
import com.ctsousa.econcilia.model.dto.GraficoVendaAnualDTO;
import com.ctsousa.econcilia.service.GraficoVendaService;
import com.ctsousa.econcilia.util.DataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class GraficoVendaAnualImpl implements GraficoVendaService<GraficoVendaAnualDTO> {

    @Override
    public GraficoVendaAnualDTO processar(List<Venda> vendas) {
        log.info("Gerando grafico anual ::: total de vendas encontradas ::: {}", vendas.size());

        GraficoVendaAnualDTO graficoVendaAnualDTO = new GraficoVendaAnualDTO();
        graficoVendaAnualDTO.setLabels(getLabels(vendas));

        Map<String, Map<YearMonth, BigDecimal>> vendasAgrupadas = vendas.stream()
                .collect(Collectors.groupingBy(venda -> venda.getEmpresa().getRazaoSocial(),
                            Collectors.groupingBy(venda -> YearMonth.from(venda.getDataPedido()),
                                    Collectors.reducing(BigDecimal.ZERO, Venda::getValorBruto, BigDecimal::add))));

        graficoVendaAnualDTO.setDataSets(getDatasSet(vendasAgrupadas, graficoVendaAnualDTO.getLabels()));

        return graficoVendaAnualDTO;
    }

    private List<DataSetDTO> getDatasSet(Map<String, Map<YearMonth, BigDecimal>> vendasAgrupadas, List<String> labels) {
        List<DataSetDTO> dataSets = new ArrayList<>();

        for (Map.Entry<String, Map<YearMonth, BigDecimal>> entry : vendasAgrupadas.entrySet()) {
            DataSetDTO dataSetDTO = new DataSetDTO();
            dataSetDTO.setLabel(entry.getKey());
            dataSetDTO.setData(new ArrayList<>());

            for (String label : labels) {
                log.info("label grafico {}", label);
                YearMonth key = DataUtil.parseMesAno(label);
                dataSetDTO.getData().add( entry.getValue().get(key));
            }

            dataSets.add(dataSetDTO);
        }

        return dataSets;
    }

    private List<String> getLabels(List<Venda> vendas) {
        List<String> labels = new ArrayList<>();

        LocalDate dtMin = vendas.stream()
                .map(Venda::getDataPedido)
                .min(LocalDate::compareTo)
                .orElse(null);

        LocalDate dtMax = vendas.stream()
                .map(Venda::getDataPedido)
                .max(LocalDate::compareTo)
                .orElse(null);

        if (dtMin == null) return new ArrayList<>();

        LocalDate dtAtual = dtMin;

        while (!dtAtual.isAfter(dtMax)) {
            String label = dtAtual.format(DateTimeFormatter.ofPattern("MMM/yyyy", Locale.forLanguageTag("pt-BR")));
            label = label.substring(0, 1).toUpperCase() + label.substring(1);
            label = label.replace(".", "");
            labels.add(label);
            dtAtual = dtAtual.plusMonths(1);
        }

        return labels;
    }
}

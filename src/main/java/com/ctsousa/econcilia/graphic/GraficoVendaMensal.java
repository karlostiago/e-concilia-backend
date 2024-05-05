package com.ctsousa.econcilia.graphic;

import com.ctsousa.econcilia.model.dto.DataSetDTO;
import com.ctsousa.econcilia.model.dto.GraficoVendaMensalDTO;
import com.ctsousa.econcilia.util.DataUtil;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class GraficoVendaMensal {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");

    private int ano = LocalDate.now().getYear();

    private int mes = LocalDate.now().getMonthValue();

    public GraficoVendaMensalDTO construir(Map<String, Map<LocalDate, BigDecimal>> mapVendas) {
        String chave = mapVendas.keySet().iterator().next();
        ano = mapVendas.get(chave).keySet().iterator().next().getYear();
        mes = mapVendas.get(chave).keySet().iterator().next().getMonthValue();

        GraficoVendaMensalDTO graficoMensal = new GraficoVendaMensalDTO();
        graficoMensal.setDataSets(new ArrayList<>());
        graficoMensal.setLabels(getLabels());

        List<DataSetDTO> datasSet = new ArrayList<>();

        for (Map.Entry<String, Map<LocalDate, BigDecimal>> entry : mapVendas.entrySet()) {
            DataSetDTO dataSet = getNovoDataSet(entry.getKey(), graficoMensal.getLabels().size());
            for (Map.Entry<LocalDate, BigDecimal> e : entry.getValue().entrySet()) {
                String diaMes = DataUtil.diaMes(e.getKey());
                int posicao = graficoMensal.getLabels().indexOf(diaMes);

                if (posicao != -1) {
                    dataSet.getData().add(posicao, e.getValue().compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.valueOf(0D) : e.getValue());
                }
            }

            datasSet.add(dataSet);
        }

        graficoMensal.setDataSets(datasSet);

        return graficoMensal;
    }

    private DataSetDTO getNovoDataSet(final String nomeEmpresa, final int tamanhoData) {
        DataSetDTO dataSet = new DataSetDTO();
        dataSet.setLabel(nomeEmpresa);
        dataSet.setData(new ArrayList<>(Collections.nCopies(tamanhoData, BigDecimal.valueOf(0D))));
        return dataSet;
    }

    private List<String> getLabels() {
        int diasNoMes = calcularTotalDiasNoMes();

        List<String> datas = new ArrayList<>(diasNoMes);

        for (int dia = 1; dia <= diasNoMes; dia++) {
            LocalDate data = LocalDate.of(ano, mes, dia);
            datas.add(data.format(formatter));
        }

        return datas;
    }

    private int calcularTotalDiasNoMes() {
        YearMonth yearMonth = YearMonth.of(ano, mes);
        return yearMonth.lengthOfMonth();
    }
}

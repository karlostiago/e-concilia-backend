package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Venda;
import com.ctsousa.econcilia.model.dto.GraficoVendaMensalDTO;
import com.ctsousa.econcilia.service.GraficoVendaService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class GraficoVendaMensalImpl implements GraficoVendaService<GraficoVendaMensalDTO>  {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");

    private int ano = LocalDate.now().getYear();

    private int mes = LocalDate.now().getMonthValue();

    @Override
    public GraficoVendaMensalDTO processar(List<Venda> vendas) {
        ano = vendas.get(0).getDataPedido().getYear();
        mes = vendas.get(0).getDataPedido().getMonthValue();

        GraficoVendaMensalDTO dto = new GraficoVendaMensalDTO();
        dto.setLabels(getLabels());

        List<GraficoVendaMensalDTO.DataSetDTO> datasSet = new ArrayList<>();

        Map<Empresa, Map<LocalDate, BigDecimal>> vendasAgrupadas = vendas.stream()
                .collect(Collectors.groupingBy(Venda::getEmpresa,
                        Collectors.groupingBy(Venda::getDataPedido,
                                Collectors.reducing(BigDecimal.valueOf(0D), Venda::getValorBruto, BigDecimal::add))));

        for (Map.Entry<Empresa, Map<LocalDate, BigDecimal>> entry : vendasAgrupadas.entrySet()) {

            GraficoVendaMensalDTO.DataSetDTO dataSet = getNovoDataSet(entry.getKey());

            List<Map.Entry<LocalDate, BigDecimal>> valoresOrdenadosPorData = entry.getValue().entrySet()
                    .stream().sorted(Map.Entry.comparingByKey())
                    .toList();

            Map<LocalDate, BigDecimal> totalizador = inicializaTotalizador();

            valoresOrdenadosPorData.forEach(vEntry -> totalizador.put(vEntry.getKey(), vEntry.getValue().setScale(2, RoundingMode.HALF_UP)));

            totalizador.forEach((chave, valor) -> dataSet.getData().add(valor.setScale(2, RoundingMode.HALF_UP)));

            datasSet.add(dataSet);
        }

        dto.setDataSets(datasSet);

        return dto;
    }

    private GraficoVendaMensalDTO.DataSetDTO getNovoDataSet(final Empresa empresa) {
        GraficoVendaMensalDTO.DataSetDTO dataSet = new GraficoVendaMensalDTO.DataSetDTO();
        dataSet.setLabel(empresa.getRazaoSocial());
        dataSet.setData(new ArrayList<>());
        return dataSet;
    }

    private Map<LocalDate, BigDecimal> inicializaTotalizador() {
        int diasNoMes = calcularTotalDiasNoMes();

        Map<LocalDate, BigDecimal> totalizadoresMap = new LinkedHashMap<>(diasNoMes);

        for (int dia = 1; dia <= diasNoMes; dia++) {
            LocalDate data = LocalDate.of(ano, mes, dia);
            totalizadoresMap.put(data, BigDecimal.valueOf(0D));
        }

        return totalizadoresMap;
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

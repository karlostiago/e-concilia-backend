package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Venda;
import com.ctsousa.econcilia.model.dto.GraficoPercentualVendaUltimo7DiaDTO;
import com.ctsousa.econcilia.model.dto.GraficoVendaMensalDTO;
import com.ctsousa.econcilia.service.AbstractGraficoVendaMeioPagamento;
import com.ctsousa.econcilia.service.GraficoVendaService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Component
public class GraficoVendaMensalImpl implements GraficoVendaService<GraficoVendaMensalDTO>  {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");

    private final int ano = LocalDate.now().getYear();

    private final int mes = LocalDate.now().getMonthValue();

    @Override
    public GraficoVendaMensalDTO processar(List<Venda> vendas) {
        GraficoVendaMensalDTO dto = new GraficoVendaMensalDTO();
        dto.setLabels(getLabels());

        List<GraficoVendaMensalDTO.DataSet> datasSet = new ArrayList<>();

        Map<Empresa, Map<LocalDate, BigDecimal>> vendasAgrupadas = vendas.stream()
                .collect(Collectors.groupingBy(Venda::getEmpresa,
                        Collectors.groupingBy(Venda::getDataPedido,
                                Collectors.reducing(BigDecimal.ZERO, Venda::getValorBruto, BigDecimal::add))));

        for (Map.Entry<Empresa, Map<LocalDate, BigDecimal>> entry : vendasAgrupadas.entrySet()) {

            GraficoVendaMensalDTO.DataSet dataSet = getNovoDataSet(entry.getKey());

            List<Map.Entry<LocalDate, BigDecimal>> valoresOrdenadosPorData = entry.getValue().entrySet()
                    .stream().sorted(Map.Entry.comparingByKey())
                    .toList();

            Map<LocalDate, BigDecimal> totalizador = inicializaTotalizador();

            valoresOrdenadosPorData.forEach(vEntry -> totalizador.put(vEntry.getKey(), vEntry.getValue()));

            totalizador.forEach((chave, valor) -> dataSet.getData().add(valor));

            datasSet.add(dataSet);
        }

        dto.setDataSets(datasSet);

        return dto;
    }

    private GraficoVendaMensalDTO.DataSet getNovoDataSet(final Empresa empresa) {
        GraficoVendaMensalDTO.DataSet dataSet = new GraficoVendaMensalDTO.DataSet();
        dataSet.setLabel(empresa.getRazaoSocial());
        dataSet.setData(new ArrayList<>());
        return dataSet;
    }

    private Map<LocalDate, BigDecimal> inicializaTotalizador() {
        int diasNoMes = calcularTotalDiasNoMes();

        Map<LocalDate, BigDecimal> totalizadoresMap = new LinkedHashMap<>(diasNoMes);

        for (int dia = 1; dia <= diasNoMes; dia++) {
            LocalDate data = LocalDate.of(ano, mes, dia);
            totalizadoresMap.put(data, BigDecimal.ZERO);
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

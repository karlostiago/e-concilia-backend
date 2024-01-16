package com.ctsousa.econcilia.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class GraficoVendaUltimo7DiaMeioPagamentoDTO {

    private List<String> labels;

    private List<BigDecimal> dataCash;

    private List<BigDecimal> dataCredit;

    private List<BigDecimal> dataDebit;

    private List<BigDecimal> dataPix;

    private List<BigDecimal> dataOther;
}

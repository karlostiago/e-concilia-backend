package com.ctsousa.econcilia.model.dto;

import com.ctsousa.econcilia.annotation.ExcludedCoverage;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@ExcludedCoverage
public class GraficoPercentualVendaFormaPagamentoDTO {

    private List<String> labels;

    private List<BigDecimal> data;
}

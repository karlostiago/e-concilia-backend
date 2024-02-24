package com.ctsousa.econcilia.model.dto;

import com.ctsousa.econcilia.Dto;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Dto
public class GraficoVendaUltimo7DiaCreditoDebitoDTO {

    private List<String> labels;

    private List<BigDecimal> dataCredit;

    private List<BigDecimal> dataDebit;
}

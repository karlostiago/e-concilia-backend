package com.ctsousa.econcilia.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class GraficoVendaUltimo7DiaDTO {

    private List<String> labels;

    private List<BigDecimal> data;
}

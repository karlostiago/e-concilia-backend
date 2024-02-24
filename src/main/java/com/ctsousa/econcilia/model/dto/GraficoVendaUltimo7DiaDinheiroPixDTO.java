package com.ctsousa.econcilia.model.dto;

import com.ctsousa.econcilia.Dto;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Dto
public class GraficoVendaUltimo7DiaDinheiroPixDTO {

    private List<String> labels;

    private List<BigDecimal> dataCash;

    private List<BigDecimal> dataPix;
}

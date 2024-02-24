package com.ctsousa.econcilia.model;

import com.ctsousa.econcilia.annotation.ExcludedCoverage;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@ExcludedCoverage
public class DivisaoPagamento {

    private String id;

    private BigDecimal valorTotal;

    private LocalDate data;
}

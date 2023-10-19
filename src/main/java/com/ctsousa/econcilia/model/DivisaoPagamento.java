package com.ctsousa.econcilia.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class DivisaoPagamento {

    private String id;

    private BigDecimal valorTotal;

    private LocalDate data;
}

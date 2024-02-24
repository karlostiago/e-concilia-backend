package com.ctsousa.econcilia.model;

import com.ctsousa.econcilia.Model;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Model
public class DivisaoPagamento {

    private String id;

    private BigDecimal valorTotal;

    private LocalDate data;
}

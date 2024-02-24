package com.ctsousa.econcilia.model;

import com.ctsousa.econcilia.Model;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

@Getter
@Setter
@Model
public class TaxaManutencao {

    private String periodoId;

    private LocalDate expectativaDataPagamento;

    private BigInteger transacaoId;

    private LocalDate dataTransacao;

    private BigDecimal valor;
}

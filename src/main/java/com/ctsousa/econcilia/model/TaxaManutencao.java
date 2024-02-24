package com.ctsousa.econcilia.model;

import com.ctsousa.econcilia.annotation.ExcludedCoverage;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

@Getter
@Setter
@ExcludedCoverage
public class TaxaManutencao {

    private String periodoId;

    private LocalDate expectativaDataPagamento;

    private BigInteger transacaoId;

    private LocalDate dataTransacao;

    private BigDecimal valor;
}

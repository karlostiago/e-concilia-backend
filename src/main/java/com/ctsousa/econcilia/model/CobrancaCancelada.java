package com.ctsousa.econcilia.model;

import com.ctsousa.econcilia.Model;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Model
public class CobrancaCancelada {

    private String nomeComerciante;

    private String planoPagamento;

    private String periodoId;

    private LocalDate expectativaDataPagamento;

    private String transacaoId;

    private LocalDate dataTransacao;

    private BigDecimal valor;
}

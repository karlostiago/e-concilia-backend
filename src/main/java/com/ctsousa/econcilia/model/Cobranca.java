package com.ctsousa.econcilia.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class Cobranca {

    private BigDecimal gmv;

    private BigDecimal totalItensPedido;

    private BigDecimal taxaEntrega;

    private BigDecimal beneficioOperadora;

    private BigDecimal beneficioComercio;

    private BigDecimal comissao;

    private BigDecimal taxaAdquirente;

    private BigDecimal taxaAdquirenteAplicada = new BigDecimal("0.0");

    private BigDecimal comissaoEntrega;

    private BigDecimal taxaComissao;

    private BigDecimal taxaComissaoAdquirente;

    private BigDecimal totalDebito;

    private BigDecimal totalCredito;

    private BigDecimal valorTaxaAntecipacao;

    private BigDecimal taxaAntecipacao;

    private BigDecimal valorTotalTaxaPedido;
}

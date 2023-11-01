package com.ctsousa.econcilia.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Setter
@Getter
public class Cobranca {

    private BigDecimal valorBruto = new BigDecimal("0.0");

    private BigDecimal valorParcial = new BigDecimal("0.0");

    private BigDecimal valorCancelado = new BigDecimal("0.0");

    private BigDecimal taxaEntrega = new BigDecimal("0.0");

    private BigDecimal beneficioOperadora = new BigDecimal("0.0");

    private BigDecimal beneficioComercio = new BigDecimal("0.0");

    private BigDecimal comissao = new BigDecimal("0.0");

    private BigDecimal taxaAdquirente = new BigDecimal("0.0");

    private BigDecimal taxaAdquirenteAplicada = new BigDecimal("0.0");

    private BigDecimal comissaoEntrega = new BigDecimal("0.0");

    private BigDecimal taxaComissao = new BigDecimal("0.0");

    private BigDecimal taxaComissaoAdquirente = new BigDecimal("0.0");

    private BigDecimal totalDebito = new BigDecimal("0.0");

    private BigDecimal totalCredito = new BigDecimal("0.0");

    private BigDecimal valorTaxaAntecipacao = new BigDecimal("0.0");

    private BigDecimal taxaAntecipacao = new BigDecimal("0.0");

    private BigDecimal taxaServico = new BigDecimal("0.0");

    public BigDecimal getTotalTaxas() {
        return this.valorBruto
                .multiply(this.taxaComissao.multiply(BigDecimal.valueOf(100)))
                .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP)
                .add(this.taxaServico)
                .add(this.taxaAdquirente.multiply(BigDecimal.valueOf(-1D)))
                .add(this.taxaAntecipacao);
    }
}

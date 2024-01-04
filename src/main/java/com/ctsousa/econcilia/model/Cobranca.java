package com.ctsousa.econcilia.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Setter
@Getter
@Entity
@Table(name = "cobranca")
@EqualsAndHashCode(callSuper = false)
public class Cobranca extends Entidade {

    @Column(name = "valor_bruto", nullable = false, columnDefinition = "decimal(19,2) default 0.0")
    private BigDecimal valorBruto = new BigDecimal("0.0");

    @Column(name = "valor_parcial", nullable = false, columnDefinition = "decimal(19,2) default 0.0")
    private BigDecimal valorParcial = new BigDecimal("0.0");

    @Column(name = "valor_cancelado", nullable = false, columnDefinition = "decimal(19,2) default 0.0")
    private BigDecimal valorCancelado = new BigDecimal("0.0");

    @Column(name = "taxa_entrega", nullable = false, columnDefinition = "decimal(19,2) default 0.0")
    private BigDecimal taxaEntrega = new BigDecimal("0.0");

    @Column(name = "beneficio_operadora", nullable = false, columnDefinition = "decimal(19,2) default 0.0")
    private BigDecimal beneficioOperadora = new BigDecimal("0.0");

    @Column(name = "beneficio_comercio", nullable = false, columnDefinition = "decimal(19,2) default 0.0")
    private BigDecimal beneficioComercio = new BigDecimal("0.0");

    @Column(name = "comissao", nullable = false, columnDefinition = "decimal(19,2) default 0.0")
    private BigDecimal comissao = new BigDecimal("0.0");

    @Column(name = "taxa_adquirente", nullable = false, columnDefinition = "decimal(19,2) default 0.0")
    private BigDecimal taxaAdquirente = new BigDecimal("0.0");

    @Column(name = "taxa_adquirente_aplicada", nullable = false, columnDefinition = "decimal(19,2) default 0.0")
    private BigDecimal taxaAdquirenteAplicada = new BigDecimal("0.0");

    @Column(name = "comissao_entrega", nullable = false, columnDefinition = "decimal(19,2) default 0.0")
    private BigDecimal comissaoEntrega = new BigDecimal("0.0");

    @Column(name = "taxa_comissao", nullable = false, columnDefinition = "decimal(19,2) default 0.0")
    private BigDecimal taxaComissao = new BigDecimal("0.0");

    @Column(name = "taxa_comissao_adquirente", nullable = false, columnDefinition = "decimal(19,2) default 0.0")
    private BigDecimal taxaComissaoAdquirente = new BigDecimal("0.0");

    @Column(name = "total_debito", nullable = false, columnDefinition = "decimal(19,2) default 0.0")
    private BigDecimal totalDebito = new BigDecimal("0.0");

    @Column(name = "total_credito", nullable = false, columnDefinition = "decimal(19,2) default 0.0")
    private BigDecimal totalCredito = new BigDecimal("0.0");

    @Column(name = "valor_taxa_antecipacao", nullable = false, columnDefinition = "decimal(19,2) default 0.0")
    private BigDecimal valorTaxaAntecipacao = new BigDecimal("0.0");

    @Column(name = "taxa_antecipacao", nullable = false, columnDefinition = "decimal(19,2) default 0.0")
    private BigDecimal taxaAntecipacao = new BigDecimal("0.0");

    @Column(name = "taxa_servico", nullable = false, columnDefinition = "decimal(19,2) default 0.0")
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

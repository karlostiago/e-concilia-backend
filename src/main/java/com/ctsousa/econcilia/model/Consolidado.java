package com.ctsousa.econcilia.model;

import com.ctsousa.econcilia.annotation.ExcludedCoverage;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "consolidado")
@EqualsAndHashCode(callSuper = false)
@ExcludedCoverage
public class Consolidado extends Entidade {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operadora_id", nullable = false)
    private Operadora operadora;

    @Column(name = "periodo", nullable = false)
    private LocalDate periodo;

    @Column(name = "total_bruto", nullable = false, precision = 10, scale = 3)
    private BigDecimal totalBruto;

    @Column(name = "total_liquido", nullable = false, precision = 10, scale = 3)
    private BigDecimal totalLiquido;

    @Column(name = "quantidade_venda", nullable = false)
    private BigInteger quantidadeVenda;

    @Column(name = "ticket_medio", nullable = false, precision = 10, scale = 3)
    private BigDecimal ticketMedio;

    @Column(name = "total_cancelado", nullable = false, precision = 10, scale = 3)
    private BigDecimal totalCancelado;

    @Column(name = "total_recebido", nullable = false, precision = 10, scale = 3)
    private BigDecimal totalRecebido;

    @Column(name = "total_taxa_entrega", nullable = false, precision = 10, scale = 3)
    private BigDecimal totalTaxaEntrega;

    @Column(name = "total_comissao", nullable = false, precision = 10, scale = 3)
    private BigDecimal totalComissao;

    @Column(name = "total_promocao", nullable = false, precision = 10, scale = 3)
    private BigDecimal totalPromocao;

    @Column(name = "total_transacao_pagamento", nullable = false, precision = 10, scale = 3)
    private BigDecimal totalTransacaoPagamento;

    @Column(name = "total_taxa_servico", nullable = false, precision = 10, scale = 3)
    private BigDecimal totalTaxaServico;

    @Column(name = "total_repasse", nullable = false, precision = 10, scale = 3)
    private BigDecimal totalRepasse;

    @Column(name = "total_taxa_manutencao", precision = 10, scale = 3)
    private BigDecimal totalTaxaManutencao;
}

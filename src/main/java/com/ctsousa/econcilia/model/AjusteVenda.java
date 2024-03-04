package com.ctsousa.econcilia.model;

import com.ctsousa.econcilia.annotation.ExcludedCoverage;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "ajuste_venda")
@EqualsAndHashCode(callSuper = false)
@ExcludedCoverage
public class AjusteVenda extends Entidade {

    @Column(name = "pedido_id", nullable = false)
    private String pedidoId;

    @Column(name = "pedido_cobranca_id", nullable = false)
    private String pedidoCobrancaId;

    @Column(name = "periodo_id", nullable = false)
    private String periodoId;

    @Column(name = "numero_documento", nullable = false)
    private String numeroDocumento;

    @Column(name = "data_pedido", nullable = false)
    private LocalDate dataPedido;

    @Column(name = "data_pedido_atualizado")
    private LocalDate dataPedidoAtualizado;

    @Column(name = "valor_ajuste", columnDefinition = "decimal(19,2) default 0.0")
    private BigDecimal valorAjuste = new BigDecimal("0.0");

    @Column(name = "data_pagamento_esperada")
    private LocalDate dataPagamentoEsperada;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "cobranca_id", nullable = false)
    private Cobranca cobranca;
}

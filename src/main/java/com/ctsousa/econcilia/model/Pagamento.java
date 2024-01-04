package com.ctsousa.econcilia.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "pagamento")
@EqualsAndHashCode(callSuper = false)
public class Pagamento extends Entidade {

    @Column(name = "periodo_id")
    private String periodoId;

    @Column(name = "tipo", nullable = false)
    private String tipo;

    @Column(name = "metodo", nullable = false)
    private String metodo;

    @Column(name = "bandeira", nullable = false)
    private String bandeira;

    @Column(name = "responsavel", nullable = false)
    private String responsavel;

    @Column(name = "numero_cartao", nullable = false)
    private String numeroCartao;

    @Column(name = "status")
    private String status;

    @Column(name = "nsu")
    private String nsu;

    @Column(name = "data_execucao_esperada")
    private LocalDate dataExecucaoEsperada;

    @Column(name = "proxima_data_execucao")
    private LocalDate proximaDataExecucao;

    @Column(name = "data_confirmada_pagamento")
    private LocalDate dataConfirmacaoPagamento;

    @Column(name = "valor_total")
    private BigDecimal valorTotal;

    @Column(name = "codigo_transacao")
    private String codigoTransacao;
}

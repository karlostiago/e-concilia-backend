package com.ctsousa.econcilia.model;

import com.ctsousa.econcilia.annotation.ExcludedCoverage;
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
@Table(name = "ocorrencia")
@EqualsAndHashCode(callSuper = false)
@ExcludedCoverage
public class Ocorrencia extends Entidade {

    @Column(name = "periodo_id", nullable = false)
    private String periodoId;

    @Column(name = "expectativa_data_pagamento")
    private LocalDate expectativaDataPagamento;

    @Column(name = "transacao_id")
    private String transacaoId;

    @Column(name = "data_transacao")
    private LocalDate dataTransacao;

    @Column(name = "valor", columnDefinition = "decimal(19,2) default 0.0")
    private BigDecimal valor;
}

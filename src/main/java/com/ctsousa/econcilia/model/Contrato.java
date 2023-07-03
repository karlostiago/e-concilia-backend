package com.ctsousa.econcilia.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "contrato")
@EqualsAndHashCode(callSuper = false)
public class Contrato extends Entidade {

    @ManyToOne( fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @ManyToOne( fetch = FetchType.LAZY)
    @JoinColumn(name = "operadora_id", nullable = false)
    private Operadora operadora;

    @Column(name = "ativo", nullable = false, columnDefinition = "boolean default false ")
    private Boolean ativo;
}




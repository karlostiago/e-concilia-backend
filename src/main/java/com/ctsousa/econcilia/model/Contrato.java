package com.ctsousa.econcilia.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "contrato")
@EqualsAndHashCode(callSuper = false)
public class Contrato extends Entidade {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operadora_id", nullable = false)
    private Operadora operadora;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "contrato", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Taxa> taxas = new ArrayList<>();

    @Column(name = "ativo", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean ativo;

    public void adicionaTaxa(Taxa taxa) {
        taxa.setContrato(this);
        taxa.setAtivo(this.getAtivo());

        this.taxas.add(taxa);
    }

    public List<Taxa> getTaxas() {
        return Collections.unmodifiableList(taxas);
    }
}




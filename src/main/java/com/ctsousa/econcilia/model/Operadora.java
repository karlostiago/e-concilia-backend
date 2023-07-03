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
@Table(name = "operadora")
@EqualsAndHashCode(callSuper = false)
public class Operadora extends Entidade {

    @Column(name = "descricao", nullable = false, length = 100, unique = true)
    private String descricao;

    @OneToMany(mappedBy = "operadora", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Taxa> taxas = new ArrayList<>();

    @Column(name = "ativo", nullable = false, columnDefinition = "boolean default false ")
    private Boolean ativo;

    public void adicionaTaxa(Taxa taxa) {
        taxa.setOperadora(this);
        taxa.setAtivo(this.getAtivo());

        this.taxas.add(taxa);
    }

    public List<Taxa> getTaxas() {
        return Collections.unmodifiableList(taxas);
    }
}



package com.ctsousa.econcilia.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static com.ctsousa.econcilia.util.StringUtil.maiuscula;

@Getter
@Setter
@Entity
@Table(name = "operadora")
@EqualsAndHashCode(callSuper = false)
public class Operadora extends Entidade {

    @Column(name = "descricao", nullable = false, length = 100, unique = true)
    private String descricao;

    @Column(name = "ativo", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean ativo;

    public Operadora() {
    }

    public Operadora(Long id) {
        setId(id);
    }

    @PrePersist
    @PreUpdate
    public void init() {
        this.descricao = maiuscula(this.descricao);
    }
}



package com.ctsousa.econcilia.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "empresa")
@EqualsAndHashCode(callSuper = false)
public class Empresa extends Entidade {

    @Column(name = "razao_social", nullable = false, length = 100)
    private String razaoSocial;

    @Column(name = "nome_fantasia", nullable = false, length = 100)
    private String nomeFantasia;

    @Column(nullable = false, length = 14)
    private String cnpj;

    @Embedded
    private Endereco endereco;

    @Embedded
    private Contato contato;
}




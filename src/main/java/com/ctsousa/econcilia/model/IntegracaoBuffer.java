package com.ctsousa.econcilia.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "integracao_buffer")
@EqualsAndHashCode(callSuper = false)
public class IntegracaoBuffer extends Entidade {

    @Column(name = "cnpj", nullable = false)
    private String cnpj;

    @Column(name = "nome_empresa", nullable = false)
    private String nomeEmpresa;

    @Column(name = "nome_operadora", nullable = false)
    private String nomeOperadora;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;
}

package com.ctsousa.econcilia.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class Endereco implements Serializable {

    @Column(nullable = false, length = 100)
    private String logradouro;

    @Column(nullable = false, length = 50)
    private String numero;

    @Column(nullable = false, length = 20)
    private String cep;

    private String complemento;

    @Column(length = 100)
    private String cidade;

    @Embedded
    private Estado estado;
}

package com.ctsousa.econcilia.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class EnderecoDTO implements Serializable {

    private String logradouro;

    private String numero;

    private String cidade;

    private EstadoDTO estado = new EstadoDTO();

    private String bairro;

    private String cep;
}

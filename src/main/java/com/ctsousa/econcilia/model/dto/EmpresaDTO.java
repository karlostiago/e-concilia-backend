package com.ctsousa.econcilia.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class EmpresaDTO implements Serializable {

    private String razaoSocial;

    private String nomeFantasia;

    private String cnpj;

    private EnderecoDTO endereco = new EnderecoDTO();

    private ContatoDTO contato = new ContatoDTO();
}

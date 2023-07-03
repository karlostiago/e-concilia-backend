package com.ctsousa.econcilia.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Getter
@Setter
public class EmpresaDTO implements Serializable {

    private Long id;

    @NotEmpty(message = "Campo razão social é obrigatório.")
    private String razaoSocial;

    @NotEmpty(message = "Campo nome fantasia é obrigatório.")
    private String nomeFantasia;

    @NotEmpty(message = "Campo cnpj é obrigatório.")
    private String cnpj;

    @Valid
    private EnderecoDTO endereco = new EnderecoDTO();

    @Valid
    private ContatoDTO contato = new ContatoDTO();

    private boolean ativo;
}
package com.ctsousa.econcilia.model.dto;

import com.ctsousa.econcilia.Dto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Getter
@Setter
@Dto
public class EnderecoDTO implements Serializable {

    @NotEmpty(message = "Campo endereço é obrigatório.")
    private String logradouro;
    @NotEmpty(message = "Campo número é obrigatório.")
    private String numero;

    private String cidade;

    @Valid
    private EstadoDTO estado = new EstadoDTO();

    private String bairro;

    @NotEmpty(message = "Campo cep é obrigatório.")
    private String cep;

    private String complemento;
}
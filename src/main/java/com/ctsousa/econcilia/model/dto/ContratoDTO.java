package com.ctsousa.econcilia.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
public class ContratoDTO implements Serializable {

    @NotNull(message = "Campo empresa é obrigatório.")
    private EmpresaDTO empresa;

    @NotNull(message = "Campo operadora é obrigatório.")
    private OperadoraDTO operadora;

    private Boolean ativo;
}

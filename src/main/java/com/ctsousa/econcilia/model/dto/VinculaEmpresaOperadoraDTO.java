package com.ctsousa.econcilia.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class VinculaEmpresaOperadoraDTO {

    private Long id;

    @NotNull(message = "Campo empresa é obrigatório.")
    private EmpresaDTO empresa;

    @NotNull(message = "Campo operadora é obrigatório.")
    private OperadoraDTO operadora;

    @NotEmpty(message = "Campo código integração é obrigatório.")
    private String codigoIntegracao;
}

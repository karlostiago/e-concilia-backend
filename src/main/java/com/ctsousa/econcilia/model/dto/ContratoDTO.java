package com.ctsousa.econcilia.model.dto;

import com.ctsousa.econcilia.annotation.ExcludedCoverage;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ExcludedCoverage
public class ContratoDTO implements Serializable {

    private Long numero;

    @NotNull(message = "Campo empresa é obrigatório.")
    private EmpresaDTO empresa;

    @NotNull(message = "Campo operadora é obrigatório.")
    private OperadoraDTO operadora;

    @Valid
    private List<TaxaDTO> taxas = new ArrayList<>();

    private Boolean ativo;
}

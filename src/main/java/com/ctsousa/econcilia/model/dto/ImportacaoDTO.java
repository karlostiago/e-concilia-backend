package com.ctsousa.econcilia.model.dto;

import com.ctsousa.econcilia.annotation.ExcludedCoverage;
import com.ctsousa.econcilia.enumaration.ImportacaoSituacao;
import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Operadora;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@ExcludedCoverage
public class ImportacaoDTO implements Serializable {

    @NotNull(message = "Campo data inicial é obrigatório.")
    private LocalDate dataInicial;

    @NotNull(message = "Campo data final é obrigatório.")
    private LocalDate dataFinal;

    @NotNull(message = "Campo empresa é obrigatório.")
    private Empresa empresa;

    @NotNull(message = "Campo operadora é obrigatório.")
    private Operadora operadora;

    private String situacao;
}
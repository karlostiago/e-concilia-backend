package com.ctsousa.econcilia.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class TaxaDTO implements Serializable {
    @NotEmpty(message = "Campo nome é obrigatório.")
    private String descricao;
    @NotNull(message = "Campo valor é obrigatório.")
    private BigDecimal valor;
    @JsonIgnore
    private OperadoraDTO operadora;

    private Boolean ativo;
}

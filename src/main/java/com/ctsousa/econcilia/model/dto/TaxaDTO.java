package com.ctsousa.econcilia.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class TaxaDTO implements Serializable {
    @NotEmpty(message = "Campo nome é obrigatório.")
    private String descricao;
    @NotNull(message = "Campo valor é obrigatório.")
    private BigDecimal valor;
    @NotNull(message = "Campo entra em vigor é obrigatório.")
    private LocalDate entraEmVigor;
    @NotNull(message = "Campo válido até é obrigatório.")
    private LocalDate validoAte;
    @JsonIgnore
    private OperadoraDTO operadora;
    private Boolean ativo;
    private Long expiraEm;
}

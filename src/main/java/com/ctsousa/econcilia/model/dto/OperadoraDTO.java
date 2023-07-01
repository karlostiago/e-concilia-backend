package com.ctsousa.econcilia.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OperadoraDTO implements Serializable {
    private Long id;
    @NotEmpty(message = "Campo nome é obrigatório.")
    private String descricao;
    @Valid
    private List<TaxaDTO> taxas = new ArrayList<>();
    private Boolean ativo;
}

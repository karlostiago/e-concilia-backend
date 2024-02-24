package com.ctsousa.econcilia.model.dto;

import com.ctsousa.econcilia.Dto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Getter
@Setter
@Dto
public class OperadoraDTO implements Serializable {

    private Long id;

    @NotEmpty(message = "Campo nome é obrigatório.")
    private String descricao;

    private Boolean ativo;
}

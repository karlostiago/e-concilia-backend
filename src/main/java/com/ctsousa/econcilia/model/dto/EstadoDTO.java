package com.ctsousa.econcilia.model.dto;

import com.ctsousa.econcilia.annotation.ExcludedCoverage;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Getter
@Setter
@ExcludedCoverage
public class EstadoDTO implements Serializable {

    @NotEmpty(message = "Campo uf é obrigatório.")
    private String uf;
}
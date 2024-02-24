package com.ctsousa.econcilia.model.dto;

import com.ctsousa.econcilia.annotation.ExcludedCoverage;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@ExcludedCoverage
public class FuncionalidadeDTO implements Serializable {
    private Integer codigo;
    private String permissao;
}

package com.ctsousa.econcilia.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class FuncionalidadeDTO implements Serializable {
    private Integer codigo;
    private String permissao;
}

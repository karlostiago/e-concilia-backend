package com.ctsousa.econcilia.model.dto;

import com.ctsousa.econcilia.Dto;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Dto
public class FuncionalidadeDTO implements Serializable {
    private Integer codigo;
    private String permissao;
}

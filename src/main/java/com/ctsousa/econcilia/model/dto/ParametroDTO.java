package com.ctsousa.econcilia.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParametroDTO {

    private Long id;
    private String empresa;
    private String operadora;
    private String tipoParametro;
    private String preFixo;
    private String descricao;
    private Boolean ativo = Boolean.FALSE;
}

package com.ctsousa.econcilia.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RelatorioTaxaDTO {

    private String descricao;
    private String valor;
    private String entraEmVigor;
    private String validoAte;
    private String ativo;
    private String tipo;
    private RelatorioDTO.Info info;
}

package com.ctsousa.econcilia.model.dto;

import com.ctsousa.econcilia.annotation.ExcludedCoverage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ExcludedCoverage
public class MensagemDTO {

    private Long id;

    private EmpresaDTO empresaDTO;

    private String conteudo;

    private Boolean resolvida;

    private Boolean lida;

    private int tipo;
}

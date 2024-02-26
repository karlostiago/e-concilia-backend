package com.ctsousa.econcilia.model.dto;

import com.ctsousa.econcilia.annotation.ExcludedCoverage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ExcludedCoverage
public class NotificacaoDTO {

    private EmpresaDTO empresaDTO;

    private String mensagem;

    private Boolean resolvida;

    private Boolean lida;
}

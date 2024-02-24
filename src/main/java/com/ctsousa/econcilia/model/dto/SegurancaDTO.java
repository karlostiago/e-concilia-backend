package com.ctsousa.econcilia.model.dto;

import com.ctsousa.econcilia.annotation.ExcludedCoverage;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ExcludedCoverage
public class SegurancaDTO implements Serializable {

    private UsuarioDTO usuario;

    private List<String> permissoes;
}

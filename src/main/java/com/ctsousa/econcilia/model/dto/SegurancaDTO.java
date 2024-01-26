package com.ctsousa.econcilia.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class SegurancaDTO implements Serializable {

    private UsuarioDTO usuario;

    private List<String> permissoes;
}

package com.ctsousa.econcilia.model.dto;

import com.ctsousa.econcilia.Dto;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Dto
public class PermissaoDTO implements Serializable {

    private Long id;

    private UsuarioDTO usuario;

    private List<FuncionalidadeDTO> funcionalidades;
}
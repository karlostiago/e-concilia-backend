package com.ctsousa.econcilia.mapper.impl;

import com.ctsousa.econcilia.mapper.ColecaoMapper;
import com.ctsousa.econcilia.mapper.DtoMapper;
import com.ctsousa.econcilia.mapper.EntidadeMapper;
import com.ctsousa.econcilia.model.*;
import com.ctsousa.econcilia.model.dto.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsuarioMapper implements EntidadeMapper<Usuario, UsuarioDTO>, DtoMapper<Usuario, UsuarioDTO>, ColecaoMapper<Usuario, UsuarioDTO> {

    @Override
    public Usuario paraEntidade(UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario();
        usuario.setId(usuarioDTO.getId());
        usuario.setNomeCompleto(usuarioDTO.getNomeCompleto());
        usuario.setSenha(usuarioDTO.getSenha());
        usuario.setEmail(usuarioDTO.getEmail());
        return usuario;
    }

    @Override
    public UsuarioDTO paraDTO(Usuario usuario) {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(usuario.getId());
        usuarioDTO.setNomeCompleto(usuario.getNomeCompleto());
        usuarioDTO.setSenha(usuario.getSenha());
        usuarioDTO.setEmail(usuario.getEmail());
        return usuarioDTO;
    }

    @Override
    public List<UsuarioDTO> paraLista(List<Usuario> usuarios) {
        return usuarios.stream()
                .map(this::paraDTO)
                .toList();
    }
}

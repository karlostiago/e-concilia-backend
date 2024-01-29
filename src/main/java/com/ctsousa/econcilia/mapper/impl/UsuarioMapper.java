package com.ctsousa.econcilia.mapper.impl;

import com.ctsousa.econcilia.enumaration.Perfil;
import com.ctsousa.econcilia.mapper.ColecaoMapper;
import com.ctsousa.econcilia.mapper.DtoMapper;
import com.ctsousa.econcilia.mapper.EntidadeMapper;
import com.ctsousa.econcilia.model.Usuario;
import com.ctsousa.econcilia.model.dto.EmpresaDTO;
import com.ctsousa.econcilia.model.dto.UsuarioDTO;
import com.ctsousa.econcilia.repository.EmpresaRepository;
import com.ctsousa.econcilia.service.EmpresaService;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

@Component
public class UsuarioMapper implements EntidadeMapper<Usuario, UsuarioDTO>, DtoMapper<Usuario, UsuarioDTO>, ColecaoMapper<Usuario, UsuarioDTO> {

    private final EmpresaService empresaService;

    private final EmpresaMapper empresaMapper;

    public UsuarioMapper(EmpresaService empresaService, EmpresaMapper empresaMapper) {
        this.empresaService = empresaService;
        this.empresaMapper = empresaMapper;
    }

    @Override
    public Usuario paraEntidade(UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario();
        usuario.setId(usuarioDTO.getId());
        usuario.setNomeCompleto(usuarioDTO.getNomeCompleto());
        usuario.setSenha(usuarioDTO.getSenha());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setLojasPermitidas(getLojasPermitidas(usuarioDTO));
        usuario.setPerfil(Perfil.porDescricao(usuarioDTO.getPerfil()));
        return usuario;
    }

    @Override
    public UsuarioDTO paraDTO(Usuario usuario) {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(usuario.getId());
        usuarioDTO.setNomeCompleto(usuario.getNomeCompleto());
        usuarioDTO.setEmail(usuario.getEmail());

        String [] idLojasPermitidas = usuario.getLojasPermitidas().split(",");
        List<EmpresaDTO> lojasPermitidas = Arrays.stream(idLojasPermitidas)
                .map(idLojaPermitida -> empresaMapper.paraDTO(empresaService.pesquisarPorId(Long.valueOf(idLojaPermitida))))
                .toList();

        usuarioDTO.setLojasPermitidas(lojasPermitidas);

        if (usuario.getPerfil() != null)
            usuarioDTO.setPerfil(usuario.getPerfil().getDescricao());

        return usuarioDTO;
    }

    @Override
    public List<UsuarioDTO> paraLista(List<Usuario> usuarios) {
        return usuarios.stream()
                .map(this::paraDTO)
                .toList();
    }

    private String getLojasPermitidas(UsuarioDTO usuarioDTO) {
        StringJoiner joiner = new StringJoiner(",");
        usuarioDTO.getLojasPermitidas().forEach(loja -> joiner.add(String.valueOf(loja.getId())));
        return joiner.toString();
    }
}

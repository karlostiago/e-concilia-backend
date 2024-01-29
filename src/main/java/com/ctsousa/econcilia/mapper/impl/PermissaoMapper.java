package com.ctsousa.econcilia.mapper.impl;

import com.ctsousa.econcilia.enumaration.Funcionalidade;
import com.ctsousa.econcilia.enumaration.TipoFuncionalidade;
import com.ctsousa.econcilia.mapper.ColecaoMapper;
import com.ctsousa.econcilia.mapper.DtoMapper;
import com.ctsousa.econcilia.mapper.EntidadeMapper;
import com.ctsousa.econcilia.model.Permissao;
import com.ctsousa.econcilia.model.dto.FuncionalidadeDTO;
import com.ctsousa.econcilia.model.dto.PermissaoDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PermissaoMapper implements EntidadeMapper<Permissao, PermissaoDTO>, DtoMapper<Permissao, PermissaoDTO>, ColecaoMapper<Permissao, PermissaoDTO> {

    private final UsuarioMapper usuarioMapper;

    public PermissaoMapper(UsuarioMapper usuarioMapper) {
        this.usuarioMapper = usuarioMapper;
    }

    @Override
    public Permissao paraEntidade(PermissaoDTO permissaoDTO) {
        Permissao permissao = new Permissao();
        if (permissaoDTO.getUsuario().getId() != null) {
            permissao.setUsuario(usuarioMapper.paraEntidade(permissaoDTO.getUsuario()));
        }
        if (!permissaoDTO.getFuncionalidades().isEmpty()) {
            permissao.setFuncionalidades(getFuncionalidades(permissaoDTO.getFuncionalidades()));
        }
        return permissao;
    }

    @Override
    public PermissaoDTO paraDTO(Permissao permissao) {
        PermissaoDTO permissaoDTO = new PermissaoDTO();
        permissaoDTO.setId(permissao.getId());
        permissaoDTO.setUsuario(usuarioMapper.paraDTO(permissao.getUsuario()));
        permissaoDTO.setFuncionalidades(getFuncionalidadesDTO(permissao.getFuncionalidades()));
        return permissaoDTO;
    }

    @Override
    public List<PermissaoDTO> paraLista(List<Permissao> permissoes) {
        return permissoes.stream()
                .map(this::paraDTO)
                .toList();
    }

    private List<Funcionalidade> getFuncionalidades(final List<FuncionalidadeDTO> funcionalidadesDTO) {
        List<Funcionalidade> funcionalidades = new ArrayList<>(funcionalidadesDTO.size());
        for (FuncionalidadeDTO funcionalidadeDTO : funcionalidadesDTO) {
            TipoFuncionalidade tipoFuncionalidade = TipoFuncionalidade.por(funcionalidadeDTO.getPermissao());
            Funcionalidade funcionalidade = Funcionalidade.por(funcionalidadeDTO.getCodigo(), tipoFuncionalidade);
            funcionalidades.add(funcionalidade);
        }
        return funcionalidades;
    }

    private List<FuncionalidadeDTO> getFuncionalidadesDTO(final List<Funcionalidade> funcionalidades) {
        List<FuncionalidadeDTO> funcionalidadesDTO = new ArrayList<>(funcionalidades.size());
        for (Funcionalidade funcionalidade : funcionalidades) {
            FuncionalidadeDTO funcionalidadeDTO = new FuncionalidadeDTO();
            funcionalidadeDTO.setCodigo(funcionalidade.getCodigo());
            funcionalidadeDTO.setPermissao(funcionalidade.getTipoFuncionalidade().name());
            funcionalidadesDTO.add(funcionalidadeDTO);
        }
        return funcionalidadesDTO;
    }
}

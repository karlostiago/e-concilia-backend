package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.enumaration.TipoFuncionalidade;
import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.model.Permissao;
import com.ctsousa.econcilia.model.Usuario;
import com.ctsousa.econcilia.repository.PermissaoRepository;
import com.ctsousa.econcilia.service.PermissaoService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ctsousa.econcilia.util.StringUtil.temValor;

@Component
public class PermissaoServiceImpl implements PermissaoService {

    private final PermissaoRepository permissaoRepository;

    public PermissaoServiceImpl(PermissaoRepository permissaoRepository) {
        this.permissaoRepository = permissaoRepository;
    }

    @Override
    public Permissao salvar(Permissao permissao) {
        if (permissao.getUsuario() == null) {
            throw new NotificacaoException("Usuário não foi informado.");
        }
        if (permissao.getFuncionalidades() == null) {
            throw new NotificacaoException("Selecione ao menos uma permissão");
        }
        if (permissao.getId() == null && permissaoRepository.existsByUsuario(permissao.getUsuario())) {
            throw new NotificacaoException("Usuário " + permissao.getUsuario().getNomeCompleto() + ", já tem permissões associadas ao seu perfil.");
        }
        return permissaoRepository.save(permissao);
    }

    @Override
    public Permissao pesquisar(Usuario usuario) {
        var permissao = permissaoRepository.porUsuario(usuario);

        if (permissao == null) {
            throw new NotificacaoException("Não existe permissão para este usuário ::: " + usuario.getNomeCompleto());
        }

        return permissao;
    }

    @Override
    public List<Permissao> pesquisar(Usuario usuario, String tipoPermissao) {

        List<Permissao> permissoes;
        String nomeCompleto = Boolean.TRUE.equals(temValor(usuario.getNomeCompleto())) ? usuario.getNomeCompleto() : null ;
        tipoPermissao = getPermissao(tipoPermissao);

        if (nomeCompleto == null && tipoPermissao == null) {
            permissoes = permissaoRepository.todas();
        }
        else {
            permissoes = permissaoRepository.pesquisar(nomeCompleto, tipoPermissao);
        }

        return agrupar(permissoes);
    }

    @Override
    public void deletar(Long id) {
        Permissao permissao = pesquisarPorId(id);
        permissaoRepository.delete(permissao);
    }

    @Override
    public void deletar(Usuario usuario) {
        Permissao permissao = permissaoRepository.porUsuario(usuario);
        if (permissao != null) {
            permissaoRepository.delete(permissao);
        }
    }

    @Override
    public Permissao pesquisarPorId(Long id) {
        Permissao permissao = permissaoRepository.porId(id);

        if (permissao == null) {
            throw new NotificacaoException("Não existe permissão com id ::: " + id);
        }

        return permissao;
    }

    @Override
    public Permissao atualizar(Long id, Permissao permissao) {
        pesquisarPorId(id);
        permissao.setId(id);
        return salvar(permissao);
    }

    private List<Permissao> agrupar(List<Permissao> permissoes) {
        Map<String, List<Permissao>> permissoesMap = permissoes.stream()
                .collect(Collectors.groupingBy(p -> p.getUsuario().getNomeCompleto()));

        List<Permissao> permissoesAgrupadas = new ArrayList<>(permissoesMap.size());
        for (Map.Entry<String, List<Permissao>> entry : permissoesMap.entrySet()) {
            permissoesAgrupadas.add(entry.getValue().get(0));
        }

        return permissoesAgrupadas;
    }

    private String getPermissao(String tipoPermissao) {
        tipoPermissao = Boolean.TRUE.equals(temValor(tipoPermissao)) ? tipoPermissao : null ;
        if (tipoPermissao != null) {
            try {
                TipoFuncionalidade tipoFuncionalidade = TipoFuncionalidade.por(tipoPermissao);
                tipoPermissao = tipoFuncionalidade != null ? tipoFuncionalidade.name() : null;
            } catch(NotificacaoException e) {
                tipoPermissao = null;
            }
        }
        return tipoPermissao;
    }
}
package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.model.Permissao;
import com.ctsousa.econcilia.model.Usuario;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PermissaoService {
    Permissao salvar(final Permissao permissao);

    List<Permissao> pesquisar(Usuario usuario, String tipoFuncionalidade);

    Permissao pesquisar(Usuario usuario);

    void deletar(Long id);

    void deletar(Usuario usuario);

    Permissao pesquisarPorId(Long id);

    Permissao atualizar(Long id, Permissao permissao);
}
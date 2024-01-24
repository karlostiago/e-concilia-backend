package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.model.Permissao;
import com.ctsousa.econcilia.model.Usuario;
import com.ctsousa.econcilia.model.dto.PermissaoDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PermissaoService {
    Permissao salvar (final Permissao permissao);

    List<Permissao> pesquisar (Usuario usuario);

    void deletar (Long id);

    Permissao pesquisarPorId (Long id);

    Permissao atualizar (Long id, PermissaoDTO permissaoDTO);
}
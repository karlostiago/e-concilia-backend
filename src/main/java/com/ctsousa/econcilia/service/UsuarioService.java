package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.model.Usuario;
import com.ctsousa.econcilia.model.dto.UsuarioDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UsuarioService {

    Usuario salvar(Usuario usuario);

    List<Usuario> pesquisar(final String nomeCompleto, final String email);

    void deletar (Long id);

    Usuario atualizar (Long id, UsuarioDTO usuarioDTO);

    Usuario pesquisarPorId (Long id);

    void confirmaEmail(String email, String confirmacaoEmail);

    void confirmaSenha(String senha, String confirmaSenha);
}

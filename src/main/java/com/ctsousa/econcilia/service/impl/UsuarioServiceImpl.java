package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.model.Usuario;
import com.ctsousa.econcilia.model.dto.UsuarioDTO;
import com.ctsousa.econcilia.repository.UsuarioRepository;
import com.ctsousa.econcilia.service.UsuarioService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario salvar(Usuario usuario) {
        if (usuario.getNomeCompleto() == null || usuario.getNomeCompleto().isEmpty()) {
            throw new NotificacaoException("Campo nome completo não pode ser null.");
        }

        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new NotificacaoException(String.format("Já existe um usuário com o e-mail %s cadastrado.", usuario.getEmail()));
        }
        return usuarioRepository.save(usuario);
    }

    @Override
    public List<Usuario> pesquisar(String nomeCompleto, String email) {
        if (null != nomeCompleto && !nomeCompleto.isEmpty() && !"null".equalsIgnoreCase(nomeCompleto)) {
            return usuarioRepository.porNomeCompleto(nomeCompleto);
        }

        if (null != email && !email.isEmpty() && !"null".equalsIgnoreCase(email)) {
            var usuario = usuarioRepository.porEmail(email);
            return usuario == null ? new ArrayList<>() : Collections.singletonList(usuario);
        }

        return usuarioRepository.findAll();
    }

    @Override
    public void deletar(Long id) {
        var usuario = pesquisarPorId(id);
        usuarioRepository.delete(usuario);
    }

    @Override
    public Usuario atualizar(Long id, UsuarioDTO usuarioDTO) {
        confirmaEmail(usuarioDTO.getEmail(), usuarioDTO.getConfirmaEmail());
        confirmaSenha(usuarioDTO.getSenha(), usuarioDTO.getConfirmaSenha());

        Usuario usuario = pesquisarPorId(id);
        BeanUtils.copyProperties(usuarioDTO, usuario, "id");
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario pesquisarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NotificacaoException(String.format("Usuário com id %d não encontrado", id)));
    }

    @Override
    public void confirmaEmail(String email, String confirmacaoEmail) {
        if (email == null) {
            throw new NotificacaoException("O campo email não pode ser null");
        }
        if (!email.equalsIgnoreCase(confirmacaoEmail)) {
            throw new NotificacaoException("O campo email não confere com o email do campo confirma email.");
        }
    }

    @Override
    public void confirmaSenha(String senha, String confirmaSenha) {
        if(senha == null) {
            throw new NotificacaoException("O campo senha não pode ser null");
        }
        if (!senha.equalsIgnoreCase(confirmaSenha)) {
            throw new NotificacaoException("O campo senha não confere com a senha do campo confirma senha.");
        }
    }
}

package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.enumaration.Perfil;
import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.model.Usuario;
import com.ctsousa.econcilia.model.dto.UsuarioDTO;
import com.ctsousa.econcilia.repository.UsuarioRepository;
import com.ctsousa.econcilia.service.PermissaoService;
import com.ctsousa.econcilia.service.SegurancaService;
import com.ctsousa.econcilia.service.UsuarioService;
import com.ctsousa.econcilia.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

@Component
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final SegurancaService segurancaService;

    private final PermissaoService permissaoService;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, SegurancaService segurancaService, PermissaoService permissaoService) {
        this.usuarioRepository = usuarioRepository;
        this.segurancaService = segurancaService;
        this.permissaoService = permissaoService;
    }

    @Override
    public Usuario salvar(Usuario usuario) {
        if (usuario.getNomeCompleto() == null || usuario.getNomeCompleto().isEmpty()) {
            throw new NotificacaoException("Campo nome completo não pode ser null.");
        }

        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new NotificacaoException(String.format("Já existe um usuário com o e-mail %s cadastrado.", usuario.getEmail()));
        }

        usuario.setSenha(segurancaService.encriptarSenha(usuario.getSenha()));
        var usuarioSalvo = usuarioRepository.save(usuario);
        adicionarPerfil(usuario);

        return usuarioSalvo;
    }

    @Override
    public List<Usuario> pesquisar(String nomeCompleto, String email) {
        if (Boolean.TRUE.equals(StringUtil.temValor(nomeCompleto))) {
            return usuarioRepository.porNomeCompleto(nomeCompleto);
        }

        if (Boolean.TRUE.equals(StringUtil.temValor(email))) {
            var usuario = usuarioRepository.porEmail(email);
            return usuario == null ? new ArrayList<>() : Collections.singletonList(usuario);
        }

        return usuarioRepository.findAll();
    }

    @Override
    public void deletar(Long id) {
        var usuario = pesquisarPorId(id);
        permissaoService.deletar(usuario);
        usuarioRepository.delete(usuario);
    }

    @Override
    public Usuario atualizar(Long id, UsuarioDTO usuarioDTO) {
        confirmaEmail(id, usuarioDTO.getEmail(), usuarioDTO.getConfirmaEmail());
        confirmaSenha(usuarioDTO.getSenha(), usuarioDTO.getConfirmaSenha());
        usuarioDTO.setSenha(segurancaService.encriptarSenha(usuarioDTO.getSenha()));

        StringJoiner joiner = new StringJoiner(",");
        usuarioDTO.getLojasPermitidas().forEach(loja -> joiner.add(String.valueOf(loja.getId())));

        Usuario usuario = pesquisarPorId(id);
        usuario.setLojasPermitidas(joiner.toString());
        usuario.setPerfil(Perfil.porDescricao(usuarioDTO.getPerfil()));

        BeanUtils.copyProperties(usuarioDTO, usuario, "id");
        adicionarPerfil(usuario);

        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario pesquisarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NotificacaoException(String.format("Usuário com id %d não encontrado", id)));
    }

    @Override
    public void confirmaEmail(Long id, String email, String confirmacaoEmail) {
        if (Boolean.FALSE.equals(StringUtil.temValor(email))) {
            throw new NotificacaoException("O campo email não pode ser null");
        }
        if (!email.equalsIgnoreCase(confirmacaoEmail)) {
            throw new NotificacaoException("O campo email não confere com o email do campo confirma email.");
        }
        if (id != null && usuarioRepository.existsEmail(id, email.toUpperCase())) {
            throw new NotificacaoException(String.format("Já existe um usuário com o e-mail %s cadastrado.", email));
        }
    }

    @Override
    public void confirmaSenha(String senha, String confirmaSenha) {
        if (Boolean.FALSE.equals(StringUtil.temValor(senha))) {
            throw new NotificacaoException("O campo senha não pode ser null");
        }
        if (!senha.equalsIgnoreCase(confirmaSenha)) {
            throw new NotificacaoException("O campo senha não confere com a senha do campo confirma senha.");
        }
    }

    private void adicionarPerfil(Usuario usuario) {
        if (usuario.getPerfil() != null) {
            Perfil perfil = usuario.getPerfil();
            perfil.aplicar(usuario, permissaoService);
        }
    }
}

package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.enumaration.Funcionalidade;
import com.ctsousa.econcilia.model.Usuario;
import com.ctsousa.econcilia.repository.PermissaoRepository;
import com.ctsousa.econcilia.repository.UsuarioRepository;
import com.ctsousa.econcilia.service.SegurancaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SegurancaServiceImpl implements SegurancaService {

    @Value("${credencial.senha}")
    private String credencial;

    private Usuario usuario;

    private List<String> permissoes;

    private final UsuarioRepository usuarioRepository;

    private final PermissaoRepository permissaoRepository;

    public SegurancaServiceImpl(UsuarioRepository usuarioRepository, PermissaoRepository permissaoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.permissaoRepository = permissaoRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        usuario = null;
        permissoes = null;

        if (username.equals("econcilia")) {
            permissoes = Funcionalidade.todas();
            return User.withUsername(username)
                    .password(encriptarSenha(credencial))
                    .authorities(permissoes.toArray(new String[0]))
                    .build();
        }

        usuario = usuarioRepository.porEmail(username);
        permissoes = permissaoRepository.porUsuario(usuario.getId());

        return User.withUsername(username)
                .password(usuario.getSenha())
                .authorities(permissoes.toArray(new String[0]))
                .build();
    }

    @Override
    public String encriptarSenha(final String senha) {
        return new BCryptPasswordEncoder().encode(senha);
    }

    @Override
    public Usuario usuarioLogado() {
        return usuario;
    }

    @Override
    public List<String> permissoes() {
        return permissoes;
    }
}

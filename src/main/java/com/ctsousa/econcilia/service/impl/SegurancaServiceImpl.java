package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.enumaration.Funcionalidade;
import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.mapper.impl.ContratoMapper;
import com.ctsousa.econcilia.model.*;
import com.ctsousa.econcilia.model.dto.ContratoDTO;
import com.ctsousa.econcilia.repository.ContratoRepository;
import com.ctsousa.econcilia.repository.PermissaoRepository;
import com.ctsousa.econcilia.repository.UsuarioRepository;
import com.ctsousa.econcilia.service.ContratoService;
import com.ctsousa.econcilia.service.SegurancaService;
import com.ctsousa.econcilia.service.TaxaService;
import com.ctsousa.econcilia.service.UsuarioService;
import lombok.Getter;
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

    @Getter
    private Usuario usuario;

    private final UsuarioRepository usuarioRepository;

    private final PermissaoRepository permissaoRepository;

    public SegurancaServiceImpl(UsuarioRepository usuarioRepository, PermissaoRepository permissaoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.permissaoRepository = permissaoRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        usuario = null;

        if (username.equals("econcilia")) {
            return User.withUsername(username)
                    .password(encriptarSenha(credencial))
                    .authorities(Funcionalidade.todas())
                    .build();
        }

        usuario = usuarioRepository.porEmail(username);
        List<String> permissoes = permissaoRepository.porUsuario(usuario.getId());

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
}

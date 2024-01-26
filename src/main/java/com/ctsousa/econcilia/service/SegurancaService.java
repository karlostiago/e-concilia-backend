package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.model.Usuario;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SegurancaService extends UserDetailsService {

    String encriptarSenha(final String senha);

    Usuario usuarioLogado();

    List<String> permissoes();
}

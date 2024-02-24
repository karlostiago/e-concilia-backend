package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.ApplicationIntegrationTest;
import com.ctsousa.econcilia.model.Usuario;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class SegurancaServiceTest extends ApplicationIntegrationTest {

    @Autowired
    private SegurancaService segurancaService;

    @Test
    void deveEncriptarSenha() {
        Assertions.assertNotNull(segurancaService.encriptarSenha("123456"));
    }

    @Test
    void deveCarregarUsuarioMaster() {
        Assertions.assertNotNull(segurancaService.loadUserByUsername("econcilia"));
    }

    @Test
    void deveRetornarUsuarioLogado() {
        logarUsuario();

        Assertions.assertNotNull(segurancaService.usuarioLogado());
    }

    @Test
    void deveRetornarPermissoesUsuarioLogado() {
        logarUsuario();

        Assertions.assertNotNull(segurancaService.permissoes());
    }

    private void logarUsuario() {
        Usuario usuario = getUsuario();
        usuarioRepository.save(usuario);
        segurancaService.loadUserByUsername(usuario.getEmail());
    }
}

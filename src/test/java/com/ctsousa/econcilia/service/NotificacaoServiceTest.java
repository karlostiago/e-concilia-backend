package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.ApplicationIntegrationTest;
import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Notificacao;
import com.ctsousa.econcilia.model.Usuario;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class NotificacaoServiceTest extends ApplicationIntegrationTest {

    @Autowired
    private NotificacaoService notificacaoService;

    private Empresa empresa;

    private Usuario usuario;

    @BeforeEach
    void setup() {
        criarSalvarEmpresa();
        empresa = empresaRepository.findAll().get(0);

        usuario = getUsuario();
        usuario.setLojasPermitidas(String.valueOf(empresa.getId()));
        usuarioRepository.save(usuario);
    }

    @Test
    void deveCadastrar() {
        Notificacao notificacao = getNotificacao(empresa);

        notificacao = notificacaoService.salvar(notificacao);

        Assertions.assertNotNull(notificacao.getId());
        Assertions.assertEquals("nova mensagem", notificacao.getMensagem());
    }

    @Test
    void deveRetornarNotificacaoExceptionQuandoEmpresaNaoInformada() {
        Notificacao notificacao = getNotificacao(null);

        var thrown = Assertions.assertThrows(NotificacaoException.class, () -> notificacaoService.salvar(notificacao));

        Assertions.assertEquals("Nenhuma empresa informada. Por gentileza informe uma empresa.", thrown.getMessage());
    }

    @Test
    void deveRetornarNotificacaoExceptionQuandoMensagemNaoInformada() {
        Notificacao notificacao = getNotificacao(empresa);
        notificacao.setMensagem(null);

        var thrown = Assertions.assertThrows(NotificacaoException.class, () -> notificacaoService.salvar(notificacao));

        Assertions.assertEquals("Nenhuma mensagem informada. Por gentileza informe uma mensagem.", thrown.getMessage());
    }

    @Test
    void deveRetornarNotificacoesLidas() {
        Notificacao notificacao = getNotificacao(empresa);
        notificacaoService.salvar(notificacao);
        notificacao.setId(null);
        notificacaoService.salvar(notificacao);
        notificacao.setId(null);
        notificacaoService.salvar(notificacao);

        Assertions.assertEquals(3, notificacaoService.pesquisarLidas(usuario).size());
    }

    @Test
    void deveRetornarNotificacoesResolvidas() {
        Notificacao notificacao = getNotificacao(empresa);
        notificacao.setId(null);
        notificacaoService.salvar(notificacao);
        notificacao.setId(null);
        notificacaoService.salvar(notificacao);

        Assertions.assertEquals(2, notificacaoService.pesquisarResolvidas(usuario).size());
    }

    @Test
    void deveMarcarComoLida() {
        Notificacao notificacao = getNotificacao(empresa);
        notificacao.setLida(Boolean.FALSE);
        notificacao = notificacaoService.salvar(notificacao);

        Assertions.assertFalse(notificacao.getLida());

        notificacaoService.marcarComoLida(notificacao.getId());

        notificacao = notificacaoService.buscarPorId(notificacao.getId());

        Assertions.assertTrue(notificacao.getLida());
    }

    @Test
    void deveMarcarComoResolvida() {
        Notificacao notificacao = getNotificacao(empresa);
        notificacao.setResolvida(Boolean.FALSE);
        notificacao = notificacaoService.salvar(notificacao);

        Assertions.assertFalse(notificacao.getResolvida());

        notificacaoService.marcarComoResolvida(notificacao.getId());

        notificacao = notificacaoService.buscarPorId(notificacao.getId());

        Assertions.assertTrue(notificacao.getResolvida());
    }

    private Notificacao getNotificacao(Empresa empresa) {
        Notificacao notificacao = new Notificacao();
        notificacao.setEmpresa(empresa);
        notificacao.setMensagem("nova mensagem");
        notificacao.setResolvida(true);
        notificacao.setLida(true);
        return notificacao;
    }
}

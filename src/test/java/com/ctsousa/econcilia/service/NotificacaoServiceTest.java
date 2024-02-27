package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.ApplicationIntegrationTest;
import com.ctsousa.econcilia.enumaration.TipoNotificacao;
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
    void deveRetornarNotificacoesNaoLidas() {
        Notificacao notificacao = getNotificacao(empresa);

        notificacao.setId(null);
        notificacao.setLida(true);
        notificacao.setResolvida(true);
        notificacaoService.salvar(notificacao);

        notificacao.setId(null);
        notificacao.setLida(false);
        notificacao.setResolvida(true);
        notificacaoService.salvar(notificacao);

        Assertions.assertEquals(1, notificacaoService.pesquisar(usuario).size());
    }

    @Test
    void deveRetornarNotificacoesNaoResolvidas() {
        Notificacao notificacao = getNotificacao(empresa);

        notificacao.setResolvida(true);
        notificacao.setLida(true);
        notificacaoService.salvar(notificacao);

        notificacao.setId(null);
        notificacao.setResolvida(false);
        notificacao.setLida(true);
        notificacaoService.salvar(notificacao);

        Assertions.assertEquals(1, notificacaoService.pesquisar(usuario).size());
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
        notificacao.setLida(Boolean.FALSE);

        notificacao = notificacaoService.salvar(notificacao);

        Assertions.assertFalse(notificacao.getResolvida());
        Assertions.assertFalse(notificacao.getLida());

        notificacaoService.marcarComoResolvida(notificacao.getId());

        notificacao = notificacaoService.buscarPorId(notificacao.getId());

        Assertions.assertTrue(notificacao.getResolvida());
        Assertions.assertTrue(notificacao.getLida());
    }

    private Notificacao getNotificacao(Empresa empresa) {
        Notificacao notificacao = new Notificacao();
        notificacao.setEmpresa(empresa);
        notificacao.setMensagem("nova mensagem");
        notificacao.setTipoNotificacao(TipoNotificacao.DIRETA.getCodigo());
        return notificacao;
    }
}

package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.ApplicationIntegrationTest;
import com.ctsousa.econcilia.enumaration.Funcionalidade;
import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.model.Permissao;
import com.ctsousa.econcilia.model.Usuario;
import com.ctsousa.econcilia.repository.PermissaoRepository;
import com.ctsousa.econcilia.repository.UsuarioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

class PermissaoServiceTest extends ApplicationIntegrationTest {

    @Autowired
    private PermissaoService permissaoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PermissaoRepository permissaoRepository;

    @AfterEach
    void destroy() {
        permissaoRepository.deleteAll();
        usuarioRepository.deleteAll();
    }

    @Test
    void deveCadastrarPermissao() {
        Permissao permissao = criarPermissao();

        Assertions.assertNotNull(permissao.getId());
    }

    @Test
    void deveRetornarNotificacaoExceptionQuandoNaoInformarUsuario() {
        Permissao permissao = new Permissao();
        var thrown = Assertions.assertThrows(NotificacaoException.class, () -> permissaoService.salvar(permissao));

        Assertions.assertEquals("Usuário não foi informado.", thrown.getMessage());
    }

    @Test
    void deveRetornarNotificacaoExceptionQuandoNaoInformarMinimoPermissao() {
        Usuario usuario = getUsuario();
        usuarioRepository.save(usuario);

        Permissao permissao = new Permissao();
        permissao.setUsuario(usuario);

        var thrown = Assertions.assertThrows(NotificacaoException.class, () -> permissaoService.salvar(permissao));

        Assertions.assertEquals("Selecione ao menos uma permissão", thrown.getMessage());
    }

    @Test
    void deveRetornarNotificacaoExceptionQuandoJaExistirPermisaoAtribuida() {
        Permissao permissao = criarPermissao();
        permissao.setId(null);

        var thrown = Assertions.assertThrows(NotificacaoException.class, () -> permissaoService.salvar(permissao));

        Assertions.assertEquals("Usuário " + permissao.getUsuario().getNomeCompleto() + ", já tem permissões associadas ao seu perfil.", thrown.getMessage());
    }

    @Test
    void devePesquisarPorUsuario() {
        Assertions.assertNotNull(permissaoService.pesquisar(criarPermissao().getUsuario()));
    }

    @Test
    void deveRetornarNotificacaoExceptionQuandoNaoEncontrarUsuarioPesquisado() {
        Usuario usuario = criarPermissao().getUsuario();
        usuario.setId(50L);

        var thrown = Assertions.assertThrows(NotificacaoException.class, () -> permissaoService.pesquisar(usuario));

        Assertions.assertEquals("Não existe permissão para este usuário ::: " + usuario.getNomeCompleto(), thrown.getMessage());
    }

    @Test
    void deveDeletarPermissaoPorId() {
        Permissao permissao = criarPermissao();

        permissaoService.deletar(permissao.getId());

        Assertions.assertEquals(0, permissaoRepository.findAll().size());
    }

    @Test
    void deveDeletarPermissaoPorUsuario() {
        Permissao permissao = criarPermissao();

        permissaoService.deletar(permissao.getUsuario());

        Assertions.assertEquals(0, permissaoRepository.findAll().size());
    }

    @Test
    void devePesquisarPermissaoPorId() {
        Permissao permissao = criarPermissao();

        Assertions.assertNotNull(permissaoService.pesquisarPorId(permissao.getId()));
    }

    @Test
    void deveRetornarNotificacaoExceptionQuandoNaoEncontrarPermissaoPorId() {
        var thrown = Assertions.assertThrows(NotificacaoException.class, () -> permissaoService.pesquisarPorId(1L));

        Assertions.assertEquals("Não existe permissão com id ::: 1", thrown.getMessage());
    }

    @Test
    void devePesquisarPermissaoPorUsuarioTipoPermissao() {
        Permissao permissao = criarPermissao();
        Usuario usuario = permissao.getUsuario();
        String tipoPermissao = "PESQUISAR_PERMISSAO";

        List<Permissao> permissaoes = permissaoService.pesquisar(usuario, tipoPermissao);

        Assertions.assertEquals(1, permissaoes.size());
    }

    @Test
    void devePesquisarPermissaoTodasPermissoesQuandoNaoInformarNomeCompletoUsuarioTipoPermissaoInexistente() {
        Permissao permissao = criarPermissao();
        Usuario usuario = permissao.getUsuario();
        usuario.setNomeCompleto(null);

        List<Permissao> permissaoes = permissaoService.pesquisar(usuario, "NAO_EXISTE");

        Assertions.assertEquals(1, permissaoes.size());
    }

    @Test
    void deveAtualizar() {
        Permissao permissao = criarPermissao();
        permissao.setFuncionalidades(List.of(Funcionalidade.ROLE_CADASTRAR_CONTRATO, Funcionalidade.ROLE_CADASTRAR_PERMISSAO));

        permissaoService.atualizar(permissao.getId(), permissao);

        Assertions.assertEquals(2, permissao.getFuncionalidades().size());
    }

    private Permissao criarPermissao() {
        Usuario usuario = getUsuario();
        usuarioRepository.save(usuario);

        Permissao permissao = new Permissao();
        permissao.setUsuario(usuario);
        permissao.setFuncionalidades(List.of(Funcionalidade.ROLE_CADASTRAR_PERMISSAO));

        return permissaoService.salvar(permissao);
    }
}

package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.AbstractApplicationTest;
import com.ctsousa.econcilia.enumaration.Perfil;
import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.mapper.impl.UsuarioMapper;
import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Usuario;
import com.ctsousa.econcilia.model.dto.UsuarioDTO;
import com.ctsousa.econcilia.repository.EmpresaRepository;
import com.ctsousa.econcilia.repository.PermissaoRepository;
import com.ctsousa.econcilia.repository.UsuarioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

class UsuarioServiceTest extends AbstractApplicationTest {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PermissaoRepository permissaoRepository;

    @Autowired
    private UsuarioMapper mapper;

    @Autowired
    private EmpresaRepository empresaRepository;

    @BeforeEach
    void setup() {
        empresaRepository.save(getEmpresa());
        usuarioService.salvar(getUsuario());
    }

    @AfterEach
    void destroy() {
        permissaoRepository.deleteAll();
        usuarioRepository.deleteAll();
        empresaRepository.deleteAll();
    }

    @Test
    void deveRetornarNotificacaoExceptionQuandoNomeCompletoIsNull() {
        Usuario usuario = new Usuario();

        var thrown = Assertions.assertThrows(NotificacaoException.class, () -> usuarioService.salvar(usuario));

        Assertions.assertEquals("Campo nome completo não pode ser null.", thrown.getMessage());
    }

    @Test
    void deveRetornarNotificacaoExceptionQuandoNomeCompletoVazio() {
        Usuario usuario = new Usuario();
        usuario.setNomeCompleto("");

        var thrown = Assertions.assertThrows(NotificacaoException.class, () -> usuarioService.salvar(usuario));

        Assertions.assertEquals("Campo nome completo não pode ser null.", thrown.getMessage());
    }

    @Test
    void deveRetornarNotificacaoExceptionQuandoExistirEmailCadastrado() {
        Assertions.assertEquals(1, usuarioRepository.findAll().size());

        Usuario usuario = getUsuario();
        usuario.setEmail(usuario.getEmail().toUpperCase());

        var thrown = Assertions.assertThrows(NotificacaoException.class, () -> usuarioService.salvar(usuario));

        Assertions.assertEquals("Já existe um usuário com o e-mail EMAIL@EMAIL.COM cadastrado.", thrown.getMessage());
    }

    @Test
    void deveCadastrarUsuarioSemPerfil() {
        Usuario usuario = getUsuario();
        usuario.setEmail("email1@email.com");
        usuario.setPerfil(null);

        usuario = usuarioService.salvar(usuario);

        Assertions.assertNotNull(usuario.getId());
        Assertions.assertNull(usuario.getPerfil());
    }

    @Test
    void deveBuscarUsuarioPorNomeCompleto() {
        List<Usuario> usuarios = usuarioService.pesquisar("USUARIO", null);

        Assertions.assertEquals(1, usuarios.size());
    }

    @Test
    void deveBuscarUsuarioPorNomeCompletoParcial() {
        List<Usuario> usuarios = usuarioService.pesquisar("USU", null);

        Assertions.assertEquals(1, usuarios.size());
    }

    @Test
    void deveBuscarUsuarioPorEmail() {
        List<Usuario> usuarios = usuarioService.pesquisar(null, "EMAIL@EMAIL.COM");

        Assertions.assertEquals(1, usuarios.size());
    }

    @Test
    void deveBuscarTodosUsuarioQuandoNomeCompletoIsNullOuEmailIsNull() {
        List<Usuario> usuarios = usuarioService.pesquisar(null, null);

        Assertions.assertEquals(1, usuarios.size());
    }

    @Test
    void deveDeletarUsuario() {
        Usuario usuario = usuarioRepository.findAll().get(0);
        usuarioService.deletar(usuario.getId());

        Assertions.assertEquals(0, usuarioRepository.findAll().size());
    }

    @Test
    void deveAtualizarUsuario() {
        Empresa empresa = empresaRepository.findAll().get(0);
        Usuario usuario = usuarioRepository.findAll().get(0);
        usuario.setLojasPermitidas(String.valueOf(empresa.getId()));

        UsuarioDTO usuarioDTO = mapper.paraDTO(usuario);
        usuarioDTO.setEmail("teste@email.com");
        usuarioDTO.setSenha(usuario.getSenha());
        usuarioDTO.setConfirmaSenha(usuario.getSenha());
        usuarioDTO.setConfirmaEmail(usuarioDTO.getEmail());

        usuario = usuarioService.atualizar(usuario.getId(), usuarioDTO);

        Assertions.assertEquals(usuarioDTO.getEmail().toUpperCase(), usuario.getEmail().toUpperCase());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = { "", "undefined", "null"})
    void deveRetornarNotificacaoExceptionQuandoAtualizarUsuarioEmail(String email) {
        var thrown = assertEmail(email);

        Assertions.assertEquals("O campo email não pode ser null", thrown.getMessage());
    }

    @Test
    void deveRetornarNotificacaoExceptionQuandoAtualizarUsuarioComConfirmacaoEmailInvalida() {
        Empresa empresa = empresaRepository.findAll().get(0);
        Usuario usuario = usuarioRepository.findAll().get(0);
        usuario.setLojasPermitidas(String.valueOf(empresa.getId()));
        usuario.setEmail("email@email.com");

        UsuarioDTO usuarioDTO = mapper.paraDTO(usuario);
        usuarioDTO.setConfirmaEmail("teste@email.com");
        Long id = usuario.getId();

        var thrown = Assertions.assertThrows(NotificacaoException.class, () -> usuarioService.atualizar(id, usuarioDTO));

        Assertions.assertEquals("O campo email não confere com o email do campo confirma email.", thrown.getMessage());
    }

    @Test
    void deveRetornarNotificacaoExceptionQuandoAtualizarUsuarioComEmailExistente() {
        Usuario usuario = getUsuario();
        usuario.setNomeCompleto("USUARIO TESTE 2");
        usuario.setEmail("novo-usuario@email.com");
        usuario.setPerfil(Perfil.CONCILIADOR);
        usuarioService.salvar(usuario);

        Assertions.assertEquals(2, usuarioRepository.findAll().size());

        Empresa empresa = empresaRepository.findAll().get(0);
        Usuario usuarioParaAtualizar = usuarioRepository.porEmail("EMAIL@EMAIL.COM");

        Assertions.assertNotNull(usuarioParaAtualizar);
        Assertions.assertEquals("EMAIL@EMAIL.COM", usuarioParaAtualizar.getEmail());

        usuarioParaAtualizar.setLojasPermitidas(String.valueOf(empresa.getId()));

        UsuarioDTO usuarioDTO = mapper.paraDTO(usuarioParaAtualizar);
        usuarioDTO.setEmail(usuario.getEmail());
        usuarioDTO.setConfirmaEmail(usuario.getEmail());
        Long id = usuarioParaAtualizar.getId();

        var thrown = Assertions.assertThrows(NotificacaoException.class, () -> usuarioService.atualizar(id, usuarioDTO));

        Assertions.assertEquals("Já existe um usuário com o e-mail NOVO-USUARIO@EMAIL.COM cadastrado.", thrown.getMessage());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = { "", "undefined", "null"})
    void deveRetornarNotificacaoExceptionQuandoAtualizarUsuarioNaoInformarSenhaValida(String senha) {
        var thrown = assertSenha(senha);

        Assertions.assertEquals("O campo senha não pode ser null", thrown.getMessage());
    }

    @Test
    void deveRetornarNotificacaoExceptionQuandoAtualizarUsuarioSenhaNaoConfirmada() {
        Empresa empresa = empresaRepository.findAll().get(0);
        Usuario usuario = usuarioRepository.findAll().get(0);
        usuario.setSenha("senha");
        usuario.setLojasPermitidas(String.valueOf(empresa.getId()));

        UsuarioDTO usuarioDTO = mapper.paraDTO(usuario);
        usuarioDTO.setConfirmaEmail(usuario.getEmail());
        usuarioDTO.setSenha(usuario.getSenha());
        usuarioDTO.setConfirmaSenha("senha 123");

        Long id = usuario.getId();

        var thrown = Assertions.assertThrows(NotificacaoException.class, () -> usuarioService.atualizar(id, usuarioDTO));

        Assertions.assertEquals("O campo senha não confere com a senha do campo confirma senha.", thrown.getMessage());
    }

    private NotificacaoException assertSenha(String senha) {
        Empresa empresa = empresaRepository.findAll().get(0);
        Usuario usuario = usuarioRepository.findAll().get(0);
        usuario.setSenha(senha);
        usuario.setLojasPermitidas(String.valueOf(empresa.getId()));

        UsuarioDTO usuarioDTO = mapper.paraDTO(usuario);
        usuarioDTO.setConfirmaEmail(usuario.getEmail());
        usuarioDTO.setSenha(usuario.getSenha());
        usuarioDTO.setConfirmaSenha(usuario.getSenha());

        Long id = usuario.getId();

        return Assertions.assertThrows(NotificacaoException.class, () -> usuarioService.atualizar(id, usuarioDTO));
    }

    private NotificacaoException assertEmail(String email) {
        Empresa empresa = empresaRepository.findAll().get(0);
        Usuario usuario = usuarioRepository.findAll().get(0);
        usuario.setLojasPermitidas(String.valueOf(empresa.getId()));
        usuario.setEmail(email);

        UsuarioDTO usuarioDTO = mapper.paraDTO(usuario);
        Long id = usuario.getId();

        return Assertions.assertThrows(NotificacaoException.class, () -> usuarioService.atualizar(id, usuarioDTO));
    }
}

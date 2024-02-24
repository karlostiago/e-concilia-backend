package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.ApplicationIntegrationTest;
import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.mapper.impl.OperadoraMapper;
import com.ctsousa.econcilia.model.Operadora;
import com.ctsousa.econcilia.model.dto.OperadoraDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OperadoraServiceTest extends ApplicationIntegrationTest {

    @Autowired
    private OperadoraService operadoraService;

    @Autowired
    private OperadoraMapper mapper;

    @Test
    void deveCadastrar() {
        Operadora operadora = criarOperadora();

        Assertions.assertNotNull(operadora.getId());
    }

    @Test
    void deveRetornarNotificacaoExceptionQuandoOperadoraNull() {
        var thrown = Assertions.assertThrows(NotificacaoException.class, () -> operadoraService.salvar(null));

        Assertions.assertEquals("Operadora não informada.", thrown.getMessage());
    }

    @Test
    void deveRetornarNotificacaoExceptionQuandoExistirOperadoraComMesmaDescricao() {
        Operadora operadora = criarOperadora();

        var thrown = Assertions.assertThrows(NotificacaoException.class, () -> operadoraService.salvar(operadora));

        Assertions.assertEquals("Já existe uma operadora com a descrição " + operadora.getDescricao(), thrown.getMessage());
    }

    @Test
    void deveBuscarOperadoraPorId() {
        Operadora operadora = criarOperadora();

        Assertions.assertNotNull(operadoraService.buscarPorID(operadora.getId()));
    }

    @Test
    void deveBuscarOperadorPorDescricao() {
        Operadora operadora = criarOperadora();

        Assertions.assertEquals(1, operadoraService.pesquisar(operadora.getDescricao()).size());
        Assertions.assertEquals(1, operadoraService.pesquisar(null).size());
    }

    @Test
    void deveRetornarNotificacaoExceptionQuandoIdNull() {
        var thrown = Assertions.assertThrows(NotificacaoException.class, () -> operadoraService.buscarPorID(99L));

        Assertions.assertEquals("Operadora não encontrada", thrown.getMessage());
    }

    @Test
    void deveAtualizarOperadora() {
        Operadora operadora = criarOperadora();
        OperadoraDTO operadoraDTO = mapper.paraDTO(operadora);
        operadoraDTO.setDescricao("Operadora teste 3");

        Assertions.assertEquals(operadoraDTO.getDescricao().toUpperCase(),
                operadoraService.atualizar(operadora.getId(), operadoraDTO).getDescricao());
    }

    @Test
    void deveDeletarOperadora() {
        Operadora operadora = criarOperadora();

        operadoraService.deletar(operadora.getId());

        Assertions.assertEquals(0, operadoraRepository.findAll().size());
    }

    @Test
    void deveRetornarNotificacaoExceptionQuandoDeletarTiverContratoAssociado() {
        criarOperadora();
        criarSalvarEmpresa();
        criarSalvarContrato();

        Long id = operadoraService.pesquisar("OPERADORA TESTE 2")
                .get(0).getId();

        var thrown = Assertions.assertThrows(NotificacaoException.class, () -> operadoraService.deletar(id));

        Assertions.assertEquals("Operadora não pode ser excluída, pois já está associada a um contrado.", thrown.getMessage());
    }

    private Operadora criarOperadora() {
        Operadora operadora = getOperadora();
        operadora.setDescricao("OPERADORA TESTE 2");
        operadoraService.salvar(operadora);
        return operadora;
    }
}

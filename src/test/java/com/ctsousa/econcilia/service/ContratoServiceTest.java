package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.ApplicationIntegrationTest;
import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.mapper.impl.ContratoMapper;
import com.ctsousa.econcilia.model.Contrato;
import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Operadora;
import com.ctsousa.econcilia.model.Taxa;
import com.ctsousa.econcilia.model.dto.ContratoDTO;
import com.ctsousa.econcilia.util.StringUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

class ContratoServiceTest extends ApplicationIntegrationTest {

    @Autowired
    private ContratoService contratoService;

    @Autowired
    private ContratoMapper mapper;

    @BeforeEach
    void setup() {
        deletarMassaDeDados();
    }

    @Test
    void deveCadastrar() {
        Assertions.assertNotNull(contratoService.salvar(getContrato()).getId());
    }

    @Test
    void deveRetornarNotificacaoExceptionQuandoJaExistirContratoParaMesmaOperadoraEmpresa() {
        Contrato contrato = getContrato();
        contratoRepository.save(contrato);

        var thrown = Assertions.assertThrows(NotificacaoException.class, () -> contratoService.salvar(contrato));

        Assertions.assertEquals("Já existe um contrato para a empresa e operadora selecionados.", thrown.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
            "1, 1",
            "1, null",
            "null, 1",
            "null, null"
    })
    void devePesquisar(String empresaId, String operadoraId) {
        Contrato contrato = getContrato();
        contrato = contratoRepository.save(contrato);

        Operadora operadora = contrato.getOperadora();
        Empresa empresa = contrato.getEmpresa();

        empresaId = !StringUtil.temValor(empresaId) ? null : empresa.getId().toString();
        operadoraId = !StringUtil.temValor(operadoraId) ? null : operadora.getId().toString();

        assertPesquisar(paraLong(empresaId), paraLong(operadoraId));
    }

    @Test
    void devePesquisarPorId() {
        Contrato contrato = getContrato();
        contrato = contratoRepository.save(contrato);

        Assertions.assertNotNull(contratoService.pesquisarPorId(contrato.getId()));
    }

    @Test
    void deveRetornarNotificacaoExceptionQuandoNaoExistirContratoPesquisadoPorId() {
        var thrown = Assertions.assertThrows(NotificacaoException.class, () -> contratoService.pesquisarPorId(99L));

        Assertions.assertEquals("Contrato com id 99 não encontrado", thrown.getMessage());
    }

    @Test
    void deveDeletar() {
        Contrato contrato = getContrato();

        contratoRepository.save(contrato);

        contratoService.deletar(contrato.getId());

        Assertions.assertEquals(0, contratoRepository.findAll().size());
    }

    @Test
    void deveAtualizar() {
        Contrato contrato = getContrato();
        contrato = contratoRepository.save(contrato);

        Assertions.assertEquals(0, contrato.getTaxas().size());

        List<Taxa> taxas = List.of(getTaxa(contrato), getTaxa(contrato));
        contrato.setTaxas(taxas);

        ContratoDTO contratoDTO = mapper.paraDTO(contrato);

        Assertions.assertEquals(contratoDTO.getTaxas().size(),
                contratoService.atualizar(contrato.getId(), contratoDTO).getTaxas().size());
    }

    @Test
    void deveAtivar() {
        Contrato contrato = getContrato();
        contrato.setAtivo(Boolean.FALSE);
        contrato = contratoRepository.save(contrato);

        Assertions.assertTrue(contratoService.ativar(contrato.getId()).getAtivo());
    }

    @Test
    void deveDesativar() {
        Contrato contrato = getContrato();
        contrato.setAtivo(Boolean.TRUE);
        contrato = contratoRepository.save(contrato);

        Assertions.assertFalse(contratoService.desativar(contrato.getId()).getAtivo());
    }

    private void assertPesquisar(Long empresaId, Long operadoraId) {
        List<Contrato> contratos = contratoService.pesquisar(empresaId, operadoraId);
        Assertions.assertEquals(1, contratos.size());
    }

    private Long paraLong(String valor) {
        if (valor == null) return null;

        return Long.valueOf(valor);
    }

    private Contrato getContrato() {
        Empresa empresa = getEmpresa();
        empresaRepository.save(empresa);

        Operadora operadora = getOperadora();
        operadoraRepository.save(operadora);

        Contrato contrato = new Contrato();
        contrato.setEmpresa(empresa);
        contrato.setOperadora(operadora);
        contrato.setAtivo(Boolean.TRUE);
        return contrato;
    }
}

package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.ApplicationIntegrationTest;
import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.mapper.impl.EmpresaMapper;
import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.dto.EmpresaDTO;
import com.ctsousa.econcilia.util.StringUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

class EmpresaServiceTest extends ApplicationIntegrationTest {

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private EmpresaMapper mapper;

    @BeforeEach
    void setup() {
        deletarMassaDeDados();
    }

    @Test
    void deveCadastrar() {
        Empresa empresa = getEmpresa();

        Assertions.assertNotNull(empresaService.salvar(empresa).getId());
    }

    @Test
    void deveRetornarNotificacaoExceptionQuandoExistirCnpjCadastrado() {
        Empresa empresa = getEmpresa();

        empresaRepository.save(empresa);

        var thrown = Assertions.assertThrows(NotificacaoException.class, () -> empresaService.salvar(empresa));

        Assertions.assertEquals("Já existe uma empresa com o cnpj " + empresa.getCnpj() + " cadastrado.", thrown.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
            "BANCO DO BRASIL, 00000000000191",
            "BANCO DO BRASIL, null",
            "null, 00000000000191",
            "null, null"
    })
    void devePesquisar(String razaoSocial, String cnpj) {
        assertPesquisar(razaoSocial, cnpj);
    }

    @Test
    void deveDeletar() {
        Empresa empresa = getEmpresa();

        empresaRepository.save(empresa);

        empresaService.deletar(empresa.getId());

        Assertions.assertEquals(0, empresaRepository.findAll().size());
    }

    @Test
    void devePesquisarPorId() {
        Empresa empresa = getEmpresa();

        empresaRepository.save(empresa);

        Assertions.assertNotNull(empresaService.pesquisarPorId(empresa.getId()));
    }

    @Test
    void deveRetornarNotificacaoExceptionQuandoNaoExistirEmpresaPesquisadoPorId() {
        var thrown = Assertions.assertThrows(NotificacaoException.class, () -> empresaService.pesquisarPorId(99L));

        Assertions.assertEquals("Empresa com id 99 não encontrado", thrown.getMessage());
    }

    @Test
    void deveAtualizar() {
        Empresa empresa = getEmpresa();
        empresa = empresaRepository.save(empresa);

        EmpresaDTO empresaDTO = mapper.paraDTO(empresa);
        empresaDTO.setRazaoSocial("EMPRESA ATUALIZADA");

        Assertions.assertEquals(empresaDTO.getRazaoSocial(),
                empresaService.atualizar(empresa.getId(), empresaDTO).getRazaoSocial());
    }

    @Test
    void deveAtivar() {
        Empresa empresa = getEmpresa();
        empresa.setAtivo(Boolean.FALSE);
        empresa = empresaRepository.save(empresa);

        Assertions.assertTrue(empresaService.ativar(empresa.getId()).isAtivo());
    }

    @Test
    void deveDesativar() {
        Empresa empresa = getEmpresa();
        empresa.setAtivo(Boolean.TRUE);
        empresa = empresaRepository.save(empresa);

        Assertions.assertFalse(empresaService.desativar(empresa.getId()).isAtivo());
    }

    private void assertPesquisar(String razaoSocial, String cnpj) {

        razaoSocial = !StringUtil.temValor(razaoSocial) ? null : razaoSocial;
        cnpj = !StringUtil.temValor(cnpj) ? null : cnpj;

        Empresa empresa = getEmpresa();
        empresaRepository.save(empresa);

        List<Empresa> empresas = empresaService.pesquisar(razaoSocial, cnpj);
        Assertions.assertEquals(1, empresas.size());
    }
}

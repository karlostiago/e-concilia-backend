package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.AbstractApplicationTest;
import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.model.Contrato;
import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Operadora;
import com.ctsousa.econcilia.model.Taxa;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class TaxaServiceTest extends AbstractApplicationTest {

    @Autowired
    TaxaService taxaService;

    @BeforeEach
    void setup() {
        criarMassaDeDados();
    }

    @AfterEach
    void destroy() {
        deletarMassaDeDados();
    }

    @Test
    void deveRetornarNotificacaoExceptionQuandoPeriodoDeVigorEstiverAntesDataAtual() {
        Taxa taxa = new Taxa();
        taxa.setEntraEmVigor(LocalDate.of(2024, 2, 20));

        var thrown = Assertions.assertThrows(NotificacaoException.class, () -> taxaService.validar(taxa));

        Assertions.assertEquals("O campo entrar em vigor não pode ser menor que a data atual.", thrown.getMessage());
    }

    @Test
    void deveRetornarNotificacaoExceptionQuandoPeriodoDeTerminoEstiverMaiorQuePeriodoInicio() {
        Taxa taxa = new Taxa();
        taxa.setEntraEmVigor(LocalDate.now().plusDays(1));
        taxa.setValidoAte(LocalDate.of(2024, 2, 19));

        var thrown = Assertions.assertThrows(NotificacaoException.class, () -> taxaService.validar(taxa));

        Assertions.assertEquals("O campo entrar em vigor não pode ser maior que o campo válido até.", thrown.getMessage());
    }

    @Test
    void deveCalcularTempoExpiracao() {
        Taxa taxa = new Taxa();
        taxa.setEntraEmVigor(LocalDate.of(2024, 2, 20));
        taxa.setValidoAte(LocalDate.of(2024, 2, 21));

        Assertions.assertEquals(1, taxaService.calcularTempoExpiracao(taxa.getEntraEmVigor(), taxa.getValidoAte()));
    }

    @Test
    void deveRetornarNotificacaoExceptionQuandoExistirUmaDuplicidade() {
        List<Taxa> taxas = new ArrayList<>(2);

        Taxa taxa = new Taxa();
        taxa.setDescricao("Taxa de teste");
        taxas.add(taxa);

        taxa = new Taxa();
        taxa.setDescricao("Taxa de teste");
        taxas.add(taxa);

        var thrown = Assertions.assertThrows(NotificacaoException.class, () -> taxaService.verificaDuplicidade(taxas));

        Assertions.assertEquals("Não é permitido duplicar taxas.", thrown.getMessage());
    }

    @Test
    void deveRetornarNotificacaoExceptionQuandoUmaTaxaEstiverComPeriodoDeInicioInvalido() {
        List<Taxa> taxas = new ArrayList<>(2);

        Taxa taxa = new Taxa();
        taxa.setDescricao("Taxa de teste 1");
        taxa.setEntraEmVigor(LocalDate.now().plusDays(1));
        taxa.setValidoAte(LocalDate.of(2024, 2, 19));
        taxas.add(taxa);

        taxa = new Taxa();
        taxa.setDescricao("Taxa de teste 2");
        taxa.setEntraEmVigor(LocalDate.now().plusDays(1));
        taxa.setValidoAte(LocalDate.now().plusDays(2));
        taxas.add(taxa);

        var thrown = Assertions.assertThrows(NotificacaoException.class, () -> taxaService.validaEntraEmVigor(taxas));

        Assertions.assertEquals("A taxa não pode entrar em vigor, pois está maior que o período de validade.", thrown.getMessage());
    }

    @Test
    void deveBuscarPorEmpresaEDescricao() {
        Empresa empresa = empresaRepository.porCnpj("00000000000191");
        Taxa taxa = taxaService.buscarPorDescricaoEmpresa("Taxa teste 1".toUpperCase(), empresa);

        Assertions.assertNotNull(taxa);
    }

    @Test
    void deveRetornarNotificacaoExceptionQuandoNaoEncontrarTaxaPorDescricaoEmpresa() {
        Empresa empresa = empresaRepository.porCnpj("00000000000191");

        var thrown = Assertions.assertThrows(NotificacaoException.class, () -> taxaService.buscarPorDescricaoEmpresa("Taxa desconhecida", empresa));

        Assertions.assertEquals("Nenhuma taxa encontrada com descrição :: Taxa desconhecida, e empresa :: " + empresa.getRazaoSocial(), thrown.getMessage());
    }

    @Test
    void deveBuscarPorContrato() {
        Empresa empresa = empresaRepository.porCnpj("00000000000191");
        Contrato contrato = contratoRepository.findByEmpresa(empresa).get(0);

        List<Taxa> taxas = taxaService.buscarPorContrato(contrato.getId());

        Assertions.assertEquals(2, taxas.size());
    }

    @Test
    void deveRetornarNotificacaoExceptionQuandoNaoEncontratTaxaPorContrato() {
        var thrown = Assertions.assertThrows(NotificacaoException.class, () -> taxaService.buscarPorContrato(99L));

        Assertions.assertEquals("Não foi encontrada nenhuma taxa para o contrato de número 99", thrown.getMessage());
    }

    @Test
    void deveBuscarPorOperadora() {
        Operadora operadora = operadoraRepository.findAll().get(0);

        List<Taxa> taxas = taxaService.buscarPorOperadora(operadora.getId());

        Assertions.assertEquals(2, taxas.size());
    }

    @Test
    void deveRetornarNotificacaoExceptionQuandoNaoEncontratTaxaPorOperadora() {
        var thrown = Assertions.assertThrows(NotificacaoException.class, () -> taxaService.buscarPorOperadora(99L));

        Assertions.assertEquals("Não foi encontrada nenhuma taxa para o operadora com id: 99", thrown.getMessage());
    }

    @Test
    void deveBuscarPorEmpresa() {
        Empresa empresa = empresaRepository.findAll().get(0);

        List<Taxa> taxas = taxaService.buscarPorEmpresa(empresa.getId());

        Assertions.assertEquals(2, taxas.size());
    }

    @Test
    void deveRetornarNotificacaoExceptionQuandoNaoEncontratTaxaPorEmpresa() {
        var thrown = Assertions.assertThrows(NotificacaoException.class, () -> taxaService.buscarPorEmpresa(99L));

        Assertions.assertEquals("Não foi encontrada nenhuma taxa para a empresa 99", thrown.getMessage());
    }

    @Test
    void deveBuscarTodasTaxas() {
        List<Taxa> taxas = taxaService.buscarTodos();

        Assertions.assertEquals(2, taxas.size());
    }

    @Test
    void deveRetornarNotificacaoExceptionQuandoNaoEncontrarNenhumaTaxa() {
        deletarMassaDeDados();

        var thrown = Assertions.assertThrows(NotificacaoException.class, () -> taxaService.buscarTodos());

        Assertions.assertEquals("Não foi encontrada nenhuma taxa", thrown.getMessage());
    }

    @Test
    void deveBuscarTaxaPorId() {
        Taxa taxa = taxaRepository.findByAll().get(0);

        Assertions.assertNotNull(taxaService.pesquisarPorId(taxa.getId()));
    }

    @Test
    void deveRetornarNotificacaoExceptionQuandoNaoEncontrarTaxaPeloIdInformado() {
        var thrown = Assertions.assertThrows(NotificacaoException.class, () -> taxaService.pesquisarPorId(99L));

        Assertions.assertEquals("Taxa com id 99 não encontrado", thrown.getMessage());
    }

    @Test
    void deveAtivarTaxa() {
        Taxa taxa = taxaRepository.findByAll().get(0);
        taxa.setAtivo(Boolean.FALSE);
        taxaRepository.save(taxa);

        taxa = taxaService.ativar(taxa.getId());

        Assertions.assertTrue(taxa.getAtivo());
    }

    @Test
    void deveDesativarTaxa() {
        Taxa taxa = taxaRepository.findByAll().get(0);
        taxa.setAtivo(Boolean.TRUE);
        taxaRepository.save(taxa);

        taxa = taxaService.desativar(taxa.getId());

        Assertions.assertFalse(taxa.getAtivo());
    }

    @Test
    void deveBuscarTaxaPorEmpresaDescricaoOperadoraValor() {
        Empresa empresa = empresaRepository.findAll().get(0);
        Operadora operadora = operadoraRepository.findAll().get(0);
        BigDecimal valor = BigDecimal.TEN;
        String descricao = "TAXA TESTE 1";

        Assertions.assertNotNull(taxaService.buscarPor(empresa, operadora, descricao, valor));
    }

    @Test
    void deveRetornarNotificacaoExceptionQuandoNaoEncontrarTaxaPorEmpresaDescricaoOperadoraValor() {
        Empresa empresa = empresaRepository.findAll().get(0);
        Operadora operadora = operadoraRepository.findAll().get(0);
        BigDecimal valor = BigDecimal.valueOf(50D);
        String descricao = "TAXA T";

        var thrown = Assertions.assertThrows(NotificacaoException.class, () -> taxaService.buscarPor(empresa, operadora, descricao, valor));

        Assertions.assertEquals("Nenhuma taxa com empresa id " + empresa.getId() + " e operadora id " + operadora.getId() + " encontrado", thrown.getMessage());
    }
}

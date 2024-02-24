package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.ApplicationIntegrationTest;
import com.ctsousa.econcilia.model.Integracao;
import com.ctsousa.econcilia.model.Operadora;
import com.ctsousa.econcilia.model.Venda;
import com.ctsousa.econcilia.model.dto.DashboardDTO;
import com.ctsousa.econcilia.service.impl.DashboardServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

class DashboardServiceTest extends ApplicationIntegrationTest {

    private DashboadService dashboadService;

    @Mock
    private IntegracaoService integracaoService;

    @BeforeEach
    void setup() {
        dashboadService = new DashboardServiceImpl(integracaoService);
    }

    @Test
    void deveBuscarVendasMensal() {
        String empresaId = "1";
        LocalDate dtInicial = LocalDate.now();
        LocalDate dtFinal = LocalDate.now().plusDays(30);

        Operadora operadora = new Operadora();
        operadora.setDescricao("ifood_mock");

        Integracao integracao = new Integracao();
        integracao.setOperadora(operadora);

        Mockito.when(integracaoService.pesquisar(Long.valueOf(empresaId), null, null))
                .thenReturn(List.of(integracao));

        List<Venda> vendas = dashboadService.buscarVendaMensal(empresaId, dtInicial, dtFinal);

        Assertions.assertFalse(vendas.isEmpty());
    }

    @Test
    void deveBuscarVendasUltimos7Dias() {
        String empresaId = "1";

        Operadora operadora = new Operadora();
        operadora.setDescricao("ifood_mock");

        Integracao integracao = new Integracao();
        integracao.setOperadora(operadora);

        Mockito.when(integracaoService.pesquisar(Long.valueOf(empresaId), null, null))
                .thenReturn(List.of(integracao));

        List<Venda> vendas = dashboadService.buscarVendasUltimos7Dias(empresaId);

        Assertions.assertFalse(vendas.isEmpty());
    }

    @Test
    void deveBuscarCarregarInformacoes() {
        String empresaId = "1";
        LocalDate dtInicial = LocalDate.now();
        LocalDate dtFinal = LocalDate.now().plusDays(30);

        Operadora operadora = new Operadora();
        operadora.setDescricao("ifood_mock");

        Integracao integracao = new Integracao();
        integracao.setOperadora(operadora);

        Mockito.when(integracaoService.pesquisar(Long.valueOf(empresaId), null, null))
                .thenReturn(List.of(integracao));

        DashboardDTO dashboardDTO = dashboadService.carregarInformacoes(empresaId, dtInicial, dtFinal);

        Assertions.assertTrue(dashboardDTO.getValorBrutoVendas().compareTo(BigDecimal.ZERO) > 0);
        Assertions.assertTrue(dashboardDTO.getQuantidadeVendas().compareTo(BigInteger.ZERO) > 0);
        Assertions.assertTrue(dashboardDTO.getTicketMedio().compareTo(BigDecimal.ZERO) > 0);
        Assertions.assertTrue(dashboardDTO.getValorCancelamento().compareTo(BigDecimal.ZERO) > 0);
        Assertions.assertTrue(dashboardDTO.getValorRecebidoLoja().compareTo(BigDecimal.ZERO) > 0);
        Assertions.assertTrue(dashboardDTO.getValorComissaoTransacao().compareTo(BigDecimal.ZERO) > 0);
        Assertions.assertTrue(dashboardDTO.getValorTaxaEntrega().compareTo(BigDecimal.ZERO) > 0);
        Assertions.assertTrue(dashboardDTO.getValorEmRepasse().compareTo(BigDecimal.ZERO) > 0);
        Assertions.assertTrue(dashboardDTO.getValorComissao().compareTo(BigDecimal.ZERO) > 0);
        Assertions.assertTrue(dashboardDTO.getValorPromocao().compareTo(BigDecimal.ZERO) > 0);
        Assertions.assertFalse(dashboardDTO.getVendas().isEmpty());
    }
}

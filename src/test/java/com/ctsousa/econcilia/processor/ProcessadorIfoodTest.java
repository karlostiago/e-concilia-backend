package com.ctsousa.econcilia.processor;

import com.ctsousa.econcilia.ApplicationIntegrationTest;
import com.ctsousa.econcilia.enumaration.FormaRecebimento;
import com.ctsousa.econcilia.model.*;
import com.ctsousa.econcilia.processor.ifood.ProcessadorIfood;
import com.ctsousa.econcilia.service.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

class ProcessadorIfoodTest extends ApplicationIntegrationTest {

    private Processador processador;

    @Mock
    private IntegracaoIfoodService integracaoIfoodService;

    @Mock
    private TaxaService taxaService;

    @Mock
    private VendaService vendaService;

    @Mock
    private OcorrenciaService ocorrenciaService;

    @Mock
    private CancelamentoService cancelamentoService;

    @Mock
    private AjusteVendaService ajusteVendaService;

    @BeforeEach
    void setup() {
        processador = new ProcessadorIfood(taxaService, vendaService, ocorrenciaService, cancelamentoService, ajusteVendaService);
        criarSalvarEmpresa();
        criarSalvarOperadora();
    }

    @Test
    void deveProcessarSemExecutarCalculo() {
        Empresa empresa = empresaRepository.findAll().get(0);
        Operadora operadora = operadoraRepository.findAll().get(0);
        Integracao integracao = getIntegracao(empresa, operadora);

        ProcessadorFiltro filtro = new ProcessadorFiltro();
        filtro.setIntegracao(integracao);
        filtro.setFormaRecebimento(FormaRecebimento.LOJA);

        Mockito.when(integracaoIfoodService.pesquisarVendas(
                        filtro.getIntegracao().getCodigoIntegracao(), filtro.getFormaPagamento(),
                        filtro.getCartaoBandeira(), "Loja",
                        filtro.getDtInicial(), filtro.getDtFinal()))
                .thenReturn(getVendas());

        processador.processar(filtro, false);
        Assertions.assertFalse(processador.vendas.isEmpty());
    }

    @Test
    void deveProcessarMasNaoRetornarVenda() {
        Empresa empresa = empresaRepository.findAll().get(0);
        Operadora operadora = operadoraRepository.findAll().get(0);
        Integracao integracao = getIntegracao(empresa, operadora);

        ProcessadorFiltro filtro = new ProcessadorFiltro();
        filtro.setIntegracao(integracao);
        filtro.setFormaRecebimento(FormaRecebimento.LOJA);

        processador.processar(filtro, false);
        Assertions.assertTrue(processador.vendas.isEmpty());
    }

    @Test
    void deveReprocessar() {
        Empresa empresa = empresaRepository.findAll().get(0);
        Operadora operadora = operadoraRepository.findAll().get(0);
        Integracao integracao = getIntegracao(empresa, operadora);

        ProcessadorFiltro filtro = new ProcessadorFiltro();
        filtro.setIntegracao(integracao);
        filtro.setFormaRecebimento(FormaRecebimento.LOJA);

        Mockito.when(integracaoIfoodService.pesquisarAjusteVendas("123456", null, null))
                        .thenReturn(getAjusteVendas());

        Mockito.when(integracaoIfoodService.pesquisarVendas(
                        filtro.getIntegracao().getCodigoIntegracao(), filtro.getFormaPagamento(),
                        filtro.getCartaoBandeira(), "Loja",
                        filtro.getDtInicial(), filtro.getDtFinal()))
                .thenReturn(getVendas());

        processador.processar(filtro, false);

        Assertions.assertEquals(2,  processador.vendas.stream()
                .filter(Venda::getReprocessada).toList().size());
    }

    @Test
    void deveProcessarComCancelamento() {
        Empresa empresa = empresaRepository.findAll().get(0);
        Operadora operadora = operadoraRepository.findAll().get(0);
        Integracao integracao = getIntegracao(empresa, operadora);

        ProcessadorFiltro filtro = new ProcessadorFiltro();
        filtro.setIntegracao(integracao);
        filtro.setFormaRecebimento(FormaRecebimento.LOJA);

        List<Cancelamento> cancelamentos = new ArrayList<>(1);
        Cancelamento cancelamento = new Cancelamento();
        cancelamento.setPedidoId("1");
        cancelamento.setValor(BigDecimal.valueOf(500D));
        cancelamentos.add(cancelamento);

        Mockito.when(integracaoIfoodService.pesquisarCancelamentos("123456", "123456"))
                .thenReturn(cancelamentos);

        Mockito.when(integracaoIfoodService.pesquisarVendas(
                        filtro.getIntegracao().getCodigoIntegracao(), filtro.getFormaPagamento(),
                        filtro.getCartaoBandeira(), "Loja",
                        filtro.getDtInicial(), filtro.getDtFinal()))
                .thenReturn(getVendas());

        processador.processar(filtro, false);

        Assertions.assertEquals(1,  processador.vendas.stream()
                .filter(venda -> venda.getCobranca().getValorCancelado().compareTo(BigDecimal.ZERO) > 0)
                .toList()
                .size());

    }

    @Test
    void deveProcessarComExecucaoCalculo() {
        Empresa empresa = empresaRepository.findAll().get(0);
        Operadora operadora = operadoraRepository.findAll().get(0);
        Integracao integracao = getIntegracao(empresa, operadora);

        ProcessadorFiltro filtro = new ProcessadorFiltro();
        filtro.setIntegracao(integracao);
        filtro.setFormaRecebimento(FormaRecebimento.LOJA);

        Mockito.when(integracaoIfoodService.pesquisarVendas(
                        filtro.getIntegracao().getCodigoIntegracao(), filtro.getFormaPagamento(),
                        filtro.getCartaoBandeira(), "Loja",
                        filtro.getDtInicial(), filtro.getDtFinal()))
                .thenReturn(getVendas());

        processador.processar(filtro, true);

        Assertions.assertEquals(BigDecimal.valueOf(3847.92D), processador.getValorTotalBruto());
        Assertions.assertEquals(BigDecimal.valueOf(769.58D), processador.getValorTotalTicketMedio());
        Assertions.assertEquals(BigDecimal.valueOf(0D), processador.getValorTotalCancelado());
        Assertions.assertEquals(BigDecimal.valueOf(3843.96D), processador.getValorTotalRecebido());
        Assertions.assertEquals(BigDecimal.valueOf(0D), processador.getValorTotalComissaoTransacaoPagamento());
        Assertions.assertEquals(BigDecimal.valueOf(0D), processador.getValorTotalComissao());
        Assertions.assertEquals(BigDecimal.valueOf(13.5D), processador.getValorTotalTaxaEntrega());
        Assertions.assertEquals(BigDecimal.valueOf(0D), processador.getValorTotalRepasse());
        Assertions.assertEquals(new BigDecimal("0.00"), processador.getValorTotalPromocao());
        Assertions.assertEquals(BigDecimal.valueOf(3843.96D), processador.getValorTotalPedido());
        Assertions.assertEquals(new BigDecimal("3840.00"), processador.getValorTotalLiquido());
        Assertions.assertEquals(Integer.valueOf(5), processador.getQuantidade());
    }
}

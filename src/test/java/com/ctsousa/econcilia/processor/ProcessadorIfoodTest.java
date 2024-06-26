package com.ctsousa.econcilia.processor;

import com.ctsousa.econcilia.ApplicationIntegrationTest;
import com.ctsousa.econcilia.enumaration.FormaRecebimento;
import com.ctsousa.econcilia.model.*;
import com.ctsousa.econcilia.processor.ifood.ProcessadorIfood;
import com.ctsousa.econcilia.service.*;
import com.ctsousa.econcilia.util.DataUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.ctsousa.econcilia.util.DataUtil.getPrimeiroDiaMes;
import static com.ctsousa.econcilia.util.DataUtil.getUltimoDiaMes;

class ProcessadorIfoodTest extends ApplicationIntegrationTest {

    private Processador processador;

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

    @Mock
    private ConsolidacaoService consolidacaoService;

    @BeforeEach
    void setup() {
        processador = new ProcessadorIfood(taxaService, vendaService, ocorrenciaService, cancelamentoService, ajusteVendaService, consolidacaoService);
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

        Mockito.when(vendaService.buscar(empresa, operadora, filtro.getDtInicial(), filtro.getDtFinal(), filtro.getFormaPagamento(), filtro.getCartaoBandeira(), "Loja"))
                .thenReturn(getVendas());

        processador.processar(filtro, false, false);
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

        processador.processar(filtro, false, false);
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

        List<Venda> vendas = getVendas();
        vendas.get(0).setEmpresa(empresa);
        vendas.get(0).setOperadora(operadora);

        LocalDate periodo = LocalDate.now().minusDays(1);

        Mockito.when(ajusteVendaService.buscar("123456",  null, null))
                        .thenReturn(getAjusteVendas());

        Mockito.when(vendaService.buscar(empresa, operadora, filtro.getDtInicial(), filtro.getDtFinal(), filtro.getFormaPagamento(), filtro.getCartaoBandeira(), "Loja"))
                .thenReturn(vendas);

        Mockito.when(consolidacaoService.temMensalidade(empresa, operadora, getPrimeiroDiaMes(periodo), getUltimoDiaMes(periodo)))
                .thenReturn(true);

        Mockito.when(consolidacaoService.buscarValorBruto(empresa, operadora, getPrimeiroDiaMes(periodo), getUltimoDiaMes(periodo)))
                .thenReturn(BigDecimal.valueOf(0D));

        processador.processar(filtro, true, false);

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

        LocalDate periodo = LocalDate.now().minusDays(1);

        List<Venda> vendas = getVendas();
        vendas.get(0).setEmpresa(empresa);
        vendas.get(0).setOperadora(operadora);

        Mockito.when(cancelamentoService.buscar("123456", "123456"))
                .thenReturn(cancelamentos);

        Mockito.when(vendaService.buscar(empresa, operadora, filtro.getDtInicial(), filtro.getDtFinal(), filtro.getFormaPagamento(), filtro.getCartaoBandeira(), "Loja"))
                .thenReturn(vendas);

        Mockito.when(consolidacaoService.temMensalidade(empresa, operadora, getPrimeiroDiaMes(periodo), getUltimoDiaMes(periodo)))
                .thenReturn(true);

        Mockito.when(consolidacaoService.buscarValorBruto(empresa, operadora, getPrimeiroDiaMes(periodo), getUltimoDiaMes(periodo)))
                .thenReturn(BigDecimal.valueOf(0D));

        processador.processar(filtro, true, false);

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

        LocalDate periodo = LocalDate.now().minusDays(1);

        List<Venda> vendas = getVendas();
        vendas.get(0).setEmpresa(empresa);
        vendas.get(0).setOperadora(operadora);

        Mockito.when(vendaService.buscar(empresa, operadora, filtro.getDtInicial(), filtro.getDtFinal(), filtro.getFormaPagamento(), filtro.getCartaoBandeira(), "Loja"))
                .thenReturn(vendas);

        Mockito.when(consolidacaoService.temMensalidade(empresa, operadora, getPrimeiroDiaMes(periodo), getUltimoDiaMes(periodo)))
                .thenReturn(true);

        Mockito.when(consolidacaoService.buscarValorBruto(empresa, operadora, getPrimeiroDiaMes(periodo), getUltimoDiaMes(periodo)))
                        .thenReturn(BigDecimal.valueOf(0D));

        processador.processar(filtro, true, false);

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

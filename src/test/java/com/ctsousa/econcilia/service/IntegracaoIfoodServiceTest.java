package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.ApplicationIntegrationTest;
import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.integration.ifood.entity.*;
import com.ctsousa.econcilia.integration.ifood.gateway.IfoodGateway;
import com.ctsousa.econcilia.mapper.impl.*;
import com.ctsousa.econcilia.model.*;
import com.ctsousa.econcilia.service.impl.IntegracaoIfoodServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class IntegracaoIfoodServiceTest extends ApplicationIntegrationTest {

    private IntegracaoIfoodService integracaoIfoodService;

    @Mock
    private VendaMapper vendaMapper;

    @Mock
    private AjusteVendaMapper ajusteVendaMapper;

    @Mock
    private PagamentoMapper pagamentoMapper;

    @Mock
    private CancelamentoMapper cancelamentoMapper;

    @Mock
    private CobrancaCanceladaMapper cobrancaCanceladaMapper;

    @Mock
    private ImpostoRendaMapper impostoRendaMapper;

    @Mock
    private OcorrenciaMapper ocorrenciaMapper;

    @Mock
    private RegistroContaReceberMapper registroContaReceberMapper;

    @Mock
    private TaxaManutencaoMapper taxaManutencaoMapper;

    @Mock
    private IntegracaoService integracaoService;

    @Mock
    private IfoodGateway ifoodGateway;

    @BeforeEach
    void setup() {
        integracaoIfoodService = new IntegracaoIfoodServiceImpl(ifoodGateway, vendaMapper, ajusteVendaMapper,
                pagamentoMapper, cancelamentoMapper, cobrancaCanceladaMapper, impostoRendaMapper, ocorrenciaMapper, registroContaReceberMapper, taxaManutencaoMapper, integracaoService);
    }

    @Test
    void deveRetornarNotificacaoExceptionQuandoPeriodoMaiorQue90Dias() {
        String codigoIntegracao = "123456";
        LocalDate dtInicial = LocalDate.now();
        LocalDate dtFinal = LocalDate.now().plusDays(150);

        var thrown = Assertions.assertThrows(NotificacaoException.class, () -> integracaoIfoodService.pesquisarTaxasManutencao(codigoIntegracao, dtInicial, dtFinal));

        Assertions.assertEquals("O período não pode ser maior que 90 dias", thrown.getMessage());
    }

    @Test
    void devePesquisarTaxaManuntencao() {
        String codigoIntegracao = "123456";
        LocalDate dtInicial = LocalDate.now();
        LocalDate dtFinal = LocalDate.now();

        Mockito.when(ifoodGateway.findMaintenanceFees(codigoIntegracao, dtInicial, dtFinal))
                .thenReturn(List.of(new MaintenanceFee()));

        Mockito.when(integracaoIfoodService.pesquisarTaxasManutencao(codigoIntegracao, dtInicial, dtFinal))
                .thenReturn(List.of(new TaxaManutencao()));

        Assertions.assertEquals(1,
                integracaoIfoodService.pesquisarTaxasManutencao(codigoIntegracao, dtInicial, dtFinal).size());

        Mockito.when(ifoodGateway.findMaintenanceFees(codigoIntegracao, dtInicial, dtFinal))
                .thenReturn(new ArrayList<>());

        Assertions.assertEquals(0,
                integracaoIfoodService.pesquisarTaxasManutencao(codigoIntegracao, dtInicial, dtFinal).size());
    }

    @Test
    void devePesquisarImpostoRenda() {
        String codigoIntegracao = "123456";
        LocalDate dtInicial = LocalDate.now();
        LocalDate dtFinal = LocalDate.now();

        Mockito.when(ifoodGateway.findIncomeTaxes(codigoIntegracao, dtInicial, dtFinal))
                .thenReturn(List.of(new IncomeTaxe()));

        Mockito.when(integracaoIfoodService.pesquisarImpostoRenda(codigoIntegracao, dtInicial, dtFinal))
                .thenReturn(List.of(new ImpostoRenda()));

        Assertions.assertEquals(1,
                integracaoIfoodService.pesquisarImpostoRenda(codigoIntegracao, dtInicial, dtFinal).size());

        Mockito.when(ifoodGateway.findIncomeTaxes(codigoIntegracao, dtInicial, dtFinal))
                .thenReturn(new ArrayList<>());

        Assertions.assertEquals(0,
                integracaoIfoodService.pesquisarImpostoRenda(codigoIntegracao, dtInicial, dtFinal).size());
    }

    @Test
    void devePesquisarRegistroContaReceber() {
        String codigoIntegracao = "123456";
        LocalDate dtInicial = LocalDate.now();
        LocalDate dtFinal = LocalDate.now();

        Mockito.when(ifoodGateway.findReceivables(codigoIntegracao, dtInicial, dtFinal))
                .thenReturn(List.of(new ReceivableRecord()));

        Mockito.when(integracaoIfoodService.pesquisarRegistroContaReceber(codigoIntegracao, dtInicial, dtFinal))
                .thenReturn(List.of(new RegistroContaReceber()));

        Assertions.assertEquals(1,
                integracaoIfoodService.pesquisarRegistroContaReceber(codigoIntegracao, dtInicial, dtFinal).size());

        Mockito.when(ifoodGateway.findReceivables(codigoIntegracao, dtInicial, dtFinal))
                .thenReturn(new ArrayList<>());

        Assertions.assertEquals(0,
                integracaoIfoodService.pesquisarRegistroContaReceber(codigoIntegracao, dtInicial, dtFinal).size());
    }

    @Test
    void devePesquisarOcorrencias() {
        String codigoIntegracao = "123456";
        LocalDate dtInicial = LocalDate.now();
        LocalDate dtFinal = LocalDate.now();

        Mockito.when(ifoodGateway.findOccurences(codigoIntegracao, dtInicial, dtFinal))
                .thenReturn(List.of(new Occurrence()));

        Mockito.when(integracaoIfoodService.pesquisarOcorrencias(codigoIntegracao, dtInicial, dtFinal))
                .thenReturn(List.of(new Ocorrencia()));

        Assertions.assertEquals(1,
                integracaoIfoodService.pesquisarOcorrencias(codigoIntegracao, dtInicial, dtFinal).size());

        Mockito.when(ifoodGateway.findOccurences(codigoIntegracao, dtInicial, dtFinal))
                .thenReturn(new ArrayList<>());

        Assertions.assertEquals(0,
                integracaoIfoodService.pesquisarOcorrencias(codigoIntegracao, dtInicial, dtFinal).size());
    }

    @Test
    void devePesquisarCobrancaCanceladas() {
        String codigoIntegracao = "123456";
        LocalDate dtInicial = LocalDate.now();
        LocalDate dtFinal = LocalDate.now();

        Mockito.when(ifoodGateway.findChargeCancellationBy(codigoIntegracao, dtInicial, dtFinal))
                .thenReturn(List.of(new ChargeCancellation()));

        Mockito.when(integracaoIfoodService.pesquisarCobrancaCanceladas(codigoIntegracao, dtInicial, dtFinal))
                .thenReturn(List.of(new CobrancaCancelada()));

        Assertions.assertEquals(1,
                integracaoIfoodService.pesquisarCobrancaCanceladas(codigoIntegracao, dtInicial, dtFinal).size());

        Mockito.when(ifoodGateway.findChargeCancellationBy(codigoIntegracao, dtInicial, dtFinal))
                .thenReturn(new ArrayList<>());

        Assertions.assertEquals(0,
                integracaoIfoodService.pesquisarCobrancaCanceladas(codigoIntegracao, dtInicial, dtFinal).size());
    }

    @Test
    void devePesquisarCancelamentos() {
        String codigoIntegracao = "123456";
        String periodoId = "123";


        Mockito.when(ifoodGateway.findCancellationBy(codigoIntegracao, periodoId))
                .thenReturn(List.of(new Cancellation()));

        Mockito.when(integracaoIfoodService.pesquisarCancelamentos(codigoIntegracao, periodoId))
                .thenReturn(List.of(new Cancelamento()));

        Assertions.assertEquals(1,
                integracaoIfoodService.pesquisarCancelamentos(codigoIntegracao, periodoId).size());

        Mockito.when(ifoodGateway.findCancellationBy(codigoIntegracao, periodoId))
                .thenReturn(new ArrayList<>());

        Assertions.assertEquals(0,
                integracaoIfoodService.pesquisarCancelamentos(codigoIntegracao, periodoId).size());
    }

    @Test
    void devePesquisarVendas() {
        String codigoIntegracao = "123456";
        LocalDate dtInicial = LocalDate.now();
        LocalDate dtFinal = LocalDate.now();

        Integracao integracao = new Integracao();
        integracao.setEmpresa(new Empresa());
        integracao.setOperadora(new Operadora());

        List<Sale> sales = new ArrayList<>();
        sales.add(new Sale());

        Assertions.assertEquals(0,
                integracaoIfoodService.pesquisarVendas(codigoIntegracao, dtInicial, dtFinal).size());

        Mockito.when(vendaMapper.paraLista(sales))
                        .thenReturn(List.of(new Venda()));

        Mockito.when(integracaoService.pesquisarPorCodigoIntegracao(codigoIntegracao))
                        .thenReturn(integracao);

        Mockito.when(ifoodGateway.findSalesBy(codigoIntegracao, dtInicial, dtFinal))
                .thenReturn(sales);

        Assertions.assertEquals(1,
                integracaoIfoodService.pesquisarVendas(codigoIntegracao, dtInicial, dtFinal).size());
    }

    @Test
    void devePesquisarAjusteVendas() {
        String codigoIntegracao = "123456";
        LocalDate dtInicial = LocalDate.now();
        LocalDate dtFinal = LocalDate.now();

        Mockito.when(ifoodGateway.findSaleAdjustmentBy(codigoIntegracao, dtInicial, dtFinal))
                .thenReturn(List.of(new SaleAdjustment()));

        Mockito.when(integracaoIfoodService.pesquisarAjusteVendas(codigoIntegracao, dtInicial, dtFinal))
                .thenReturn(List.of(new AjusteVenda()));

        Assertions.assertEquals(1,
                integracaoIfoodService.pesquisarAjusteVendas(codigoIntegracao, dtInicial, dtFinal).size());

        Mockito.when(ifoodGateway.findSaleAdjustmentBy(codigoIntegracao, dtInicial, dtFinal))
                .thenReturn(new ArrayList<>());

        Assertions.assertEquals(0,
                integracaoIfoodService.pesquisarAjusteVendas(codigoIntegracao, dtInicial, dtFinal).size());
    }

    @Test
    void devePesquisarPagamentos() {
        String codigoIntegracao = "123456";
        LocalDate dtInicial = LocalDate.now();
        LocalDate dtFinal = LocalDate.now();

        Mockito.when(ifoodGateway.findPaymentBy(codigoIntegracao, dtInicial, dtFinal))
                .thenReturn(List.of(new Payment()));

        Mockito.when(integracaoIfoodService.pesquisarPagamentos(codigoIntegracao, dtInicial, dtFinal))
                .thenReturn(List.of(new Pagamento()));

        Assertions.assertEquals(1,
                integracaoIfoodService.pesquisarPagamentos(codigoIntegracao, dtInicial, dtFinal).size());

        Mockito.when(ifoodGateway.findPaymentBy(codigoIntegracao, dtInicial, dtFinal))
                .thenReturn(new ArrayList<>());

        Assertions.assertEquals(0,
                integracaoIfoodService.pesquisarPagamentos(codigoIntegracao, dtInicial, dtFinal).size());
    }
}

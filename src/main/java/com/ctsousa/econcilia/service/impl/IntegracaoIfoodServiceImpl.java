package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.filter.VendaFilter;
import com.ctsousa.econcilia.integration.ifood.entity.*;
import com.ctsousa.econcilia.integration.ifood.gateway.IfoodGateway;
import com.ctsousa.econcilia.mapper.impl.*;
import com.ctsousa.econcilia.model.*;
import com.ctsousa.econcilia.service.IntegracaoIfoodService;
import com.ctsousa.econcilia.service.IntegracaoService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Component
public class IntegracaoIfoodServiceImpl implements IntegracaoIfoodService {

    private final IfoodGateway ifoodGateway;

    private final VendaMapper vendaMapper;

    private final AjusteVendaMapper ajusteVendaMapper;

    private final PagamentoMapper pagamentoMapper;

    private final CancelamentoMapper cancelamentoMapper;

    private final CobrancaCanceladaMapper cobrancaCanceladaMapper;

    private final ImpostoRendaMapper impostoRendaMapper;

    private final OcorrenciaMapper ocorrenciaMapper;

    private final RegistroContaReceberMapper registroContaReceberMapper;

    private final TaxaManutencaoMapper taxaManutencaoMapper;

    private final IntegracaoService integracaoService;

    public IntegracaoIfoodServiceImpl(IfoodGateway ifoodGateway,
                                      VendaMapper vendaMapper,
                                      AjusteVendaMapper ajusteVendaMapper,
                                      PagamentoMapper pagamentoMapper,
                                      CancelamentoMapper cancelamentoMapper,
                                      CobrancaCanceladaMapper cobrancaCanceladaMapper,
                                      ImpostoRendaMapper impostoRendaMapper,
                                      OcorrenciaMapper ocorrenciaMapper,
                                      RegistroContaReceberMapper registroContaReceberMapper,
                                      TaxaManutencaoMapper taxaManutencaoMapper,
                                      IntegracaoService integracaoService) {
        this.ifoodGateway = ifoodGateway;
        this.vendaMapper = vendaMapper;
        this.ajusteVendaMapper = ajusteVendaMapper;
        this.pagamentoMapper = pagamentoMapper;
        this.cancelamentoMapper = cancelamentoMapper;
        this.cobrancaCanceladaMapper = cobrancaCanceladaMapper;
        this.impostoRendaMapper = impostoRendaMapper;
        this.ocorrenciaMapper = ocorrenciaMapper;
        this.registroContaReceberMapper = registroContaReceberMapper;
        this.taxaManutencaoMapper = taxaManutencaoMapper;
        this.integracaoService = integracaoService;
    }

    @Override
    public List<TaxaManutencao> pesquisarTaxasManutencao(String codigoIntegracao, LocalDate dtInicial, LocalDate dtFinal) {

        validaPeriodoMaior90Dias(dtInicial, dtFinal);

        List<MaintenanceFee> maintenanceFees = ifoodGateway.findMaintenanceFees(codigoIntegracao, dtInicial, dtFinal);

        if (maintenanceFees.isEmpty()) {
            return new ArrayList<>();
        }

        return taxaManutencaoMapper.paraLista(maintenanceFees);
    }

    @Override
    public List<ImpostoRenda> pesquisarImpostoRenda(String codigoIntegracao, LocalDate dtInicial, LocalDate dtFinal) {

        validaPeriodoMaior90Dias(dtInicial, dtFinal);

        List<IncomeTaxe> incomeTaxes = ifoodGateway.findIncomeTaxes(codigoIntegracao, dtInicial, dtFinal);

        if (incomeTaxes.isEmpty()) {
            return new ArrayList<>();
        }

        return impostoRendaMapper.paraLista(incomeTaxes);
    }

    @Override
    public List<RegistroContaReceber> pesquisarRegistroContaReceber(String codigoIntegracao, LocalDate dtInicial, LocalDate dtFinal) {

        validaPeriodoMaior90Dias(dtInicial, dtFinal);

        List<ReceivableRecord> receivableRecords = ifoodGateway.findReceivables(codigoIntegracao, dtInicial, dtFinal);

        if (receivableRecords.isEmpty()) {
            return new ArrayList<>();
        }

        return registroContaReceberMapper.paraLista(receivableRecords);
    }

    @Override
    public List<Ocorrencia> pesquisarOcorrencias(String codigoIntegracao, LocalDate dtInicial, LocalDate dtFinal) {

        validaPeriodoMaior90Dias(dtInicial, dtFinal);

        List<Occurrence> occurrences = ifoodGateway.findOccurences(codigoIntegracao, dtInicial, dtFinal);

        if (occurrences.isEmpty()) {
            return new ArrayList<>();
        }

        return ocorrenciaMapper.paraLista(occurrences);
    }

    @Override
    public List<CobrancaCancelada> pesquisarCobrancaCanceladas(String codigoIntegracao, LocalDate dtInicial, LocalDate dtFinal) {

        validaPeriodoMaior90Dias(dtInicial, dtFinal);

        List<ChargeCancellation> chargeCancellations = ifoodGateway.findChargeCancellationBy(codigoIntegracao, dtInicial, dtFinal);

        if (chargeCancellations.isEmpty()) {
            return new ArrayList<>();
        }

        return cobrancaCanceladaMapper.paraLista(chargeCancellations);
    }

    @Override
    public List<Cancelamento> pesquisarCancelamentos(String codigoIntegracao, String periodoId) {
        List<Cancellation> cancellations = ifoodGateway.findCancellationBy(codigoIntegracao, periodoId);

        if (cancellations.isEmpty()) {
            return new ArrayList<>();
        }

        return cancelamentoMapper.paraLista(cancellations);
    }

    @Override
    public List<Venda> pesquisarVendas(String codigoIntegracao, LocalDate dtInicial, LocalDate dtFinal) {
        validaPeriodoMaior90Dias(dtInicial, dtFinal);

        List<Sale> sales = ifoodGateway.findSalesBy(codigoIntegracao, dtInicial, dtFinal);

        if (sales.isEmpty()) {
            return new ArrayList<>();
        }

        Integracao integracao = integracaoService.pesquisarPorCodigoIntegracao(codigoIntegracao);
        Empresa empresa = integracao.getEmpresa();
        Operadora operadora = integracao.getOperadora();

        List<Venda> vendas = vendaMapper.paraLista(sales);

        vendas.forEach(venda -> {
            venda.setEmpresa(empresa);
            venda.setOperadora(operadora);
        });

        return vendas;
    }

    @Override
    public List<AjusteVenda> pesquisarAjusteVendas(String codigoIntegracao, LocalDate dtInicial, LocalDate dtFinal) {
        validaPeriodoMaior90Dias(dtInicial, dtFinal);

        List<SaleAdjustment> saleAdjustments = ifoodGateway.findSaleAdjustmentBy(codigoIntegracao, dtInicial, dtFinal);

        if (saleAdjustments.isEmpty()) {
            return new ArrayList<>();
        }

        return ajusteVendaMapper.paraLista(saleAdjustments);
    }

    @Override
    public List<Pagamento> pesquisarPagamentos(String codigoIntegracao, LocalDate dtInicial, LocalDate dtFinal) {
        validaPeriodoMaior90Dias(dtInicial, dtFinal);

        List<Payment> payments = ifoodGateway.findPaymentBy(codigoIntegracao, dtInicial, dtFinal);

        if (payments.isEmpty()) {
            return new ArrayList<>();
        }

        return pagamentoMapper.paraLista(payments);
    }

    private List<Venda> filtrarVendas(final List<Venda> vendas, final String metodoPagamento, final String bandeira, final String tipoRecebimento) {
        return new VendaFilter(vendas, bandeira, metodoPagamento, tipoRecebimento)
                .porBandeira()
                .porMetodoPagamento()
                .porMetodoPagamentoBandeira()
                .porTipoRecebimento()
                .getVendasFiltradas();
    }

    private void validaPeriodoMaior90Dias(final LocalDate dtInicial, final LocalDate dtFinal) {
        long dias = ChronoUnit.DAYS.between(dtInicial, dtFinal);

        if (dias > 90) {
            throw new NotificacaoException("O período não pode ser maior que 90 dias");
        }
    }
}

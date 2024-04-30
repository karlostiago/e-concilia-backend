package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.filter.VendaFilter;
import com.ctsousa.econcilia.integration.ifood.entity.*;
import com.ctsousa.econcilia.integration.ifood.gateway.IfoodGateway;
import com.ctsousa.econcilia.mapper.impl.*;
import com.ctsousa.econcilia.model.*;
import com.ctsousa.econcilia.service.LocalizadorRegistroIfood;
import com.ctsousa.econcilia.service.IntegracaoIfoodService;
import com.ctsousa.econcilia.service.IntegracaoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Slf4j
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
        List<MaintenanceFee> maintenanceFees = pesquisar(codigoIntegracao, dtInicial, dtFinal, ifoodGateway::findMaintenanceFees);

        if (maintenanceFees.isEmpty()) {
            return new ArrayList<>();
        }

        return taxaManutencaoMapper.paraLista(maintenanceFees);
    }

    @Override
    public List<ImpostoRenda> pesquisarImpostoRenda(String codigoIntegracao, LocalDate dtInicial, LocalDate dtFinal) {
        List<IncomeTaxe> incomeTaxes = pesquisar(codigoIntegracao, dtInicial, dtFinal, ifoodGateway::findIncomeTaxes);

        if (incomeTaxes.isEmpty()) {
            return new ArrayList<>();
        }

        return impostoRendaMapper.paraLista(incomeTaxes);
    }

    @Override
    public List<RegistroContaReceber> pesquisarRegistroContaReceber(String codigoIntegracao, LocalDate dtInicial, LocalDate dtFinal) {
        List<ReceivableRecord> receivableRecords = pesquisar(codigoIntegracao, dtInicial, dtFinal, ifoodGateway::findReceivables);

        if (receivableRecords.isEmpty()) {
            return new ArrayList<>();
        }

        return registroContaReceberMapper.paraLista(receivableRecords);
    }

    @Override
    public List<Ocorrencia> pesquisarOcorrencias(String codigoIntegracao, LocalDate dtInicial, LocalDate dtFinal) {
        List<Occurrence> occurrences = pesquisar(codigoIntegracao, dtInicial, dtFinal, ifoodGateway::findOccurences);

        if (occurrences.isEmpty()) {
            return new ArrayList<>();
        }

        return ocorrenciaMapper.paraLista(occurrences);
    }

    @Override
    public List<CobrancaCancelada> pesquisarCobrancaCanceladas(String codigoIntegracao, LocalDate dtInicial, LocalDate dtFinal) {
        List<ChargeCancellation> chargeCancellations = pesquisar(codigoIntegracao, dtInicial, dtFinal, ifoodGateway::findChargeCancellationBy);

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
    public List<Cancelamento> pesquisarCancelamentos(String codigoIntegracao, LocalDate dtInicial, LocalDate dtFinal) {
        List<Cancellation> cancellations = pesquisar(codigoIntegracao, dtInicial, dtFinal, ifoodGateway::findCancellationBy);

        if (cancellations.isEmpty()) {
            return new ArrayList<>();
        }

        return cancelamentoMapper.paraLista(cancellations);
    }

    @Override
    public List<Venda> pesquisarVendas(String codigoIntegracao, LocalDate dtInicial, LocalDate dtFinal) {
        List<Sale> sales = pesquisar(codigoIntegracao, dtInicial, dtFinal, ifoodGateway::findSalesBy);

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
        List<SaleAdjustment> saleAdjustments = pesquisar(codigoIntegracao, dtInicial, dtFinal, ifoodGateway::findSaleAdjustmentBy);

        if (saleAdjustments.isEmpty()) {
            return new ArrayList<>();
        }

        return ajusteVendaMapper.paraLista(saleAdjustments);
    }

    @Override
    public List<Pagamento> pesquisarPagamentos(String codigoIntegracao, LocalDate dtInicial, LocalDate dtFinal) {
        List<Payment> payments = pesquisar(codigoIntegracao, dtInicial, dtFinal, ifoodGateway::findPaymentBy);

        if (payments.isEmpty()) {
            return new ArrayList<>();
        }

        return pagamentoMapper.paraLista(payments);
    }

    private <T> List <T> pesquisar(String codigoIntegracao, LocalDate dtInicial, LocalDate dtFinal, LocalizadorRegistroIfood<T> localizador) {
        validaPeriodoMaior90Dias(dtInicial, dtFinal);

        int maximoTentativa = 3;
        int tentativa = 0;

        boolean ocorreuErro;

        List<T> registros = new ArrayList<>();

        do {
            try {
                registros = localizador.executar(codigoIntegracao, dtInicial, dtFinal);
                ocorreuErro = false;
            } catch (Exception e) {
                log.info("Ocorreu um erro ao buscar registros na integração do ifood ::: será realizado uma tentativa {} de no maximo {} :::", tentativa, maximoTentativa);
                ocorreuErro = true;
                tentativa++;
                pausarExecucao();
            }

            if (tentativa == maximoTentativa) break;

        } while (ocorreuErro);

        return registros;
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

    private void pausarExecucao() {
        try {
            Thread.sleep(30000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

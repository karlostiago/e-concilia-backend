package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.integration.ifood.IfoodGateway;
import com.ctsousa.econcilia.mapper.impl.IntegracaoMapper;
import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Integracao;
import com.ctsousa.econcilia.model.Operadora;
import com.ctsousa.econcilia.model.dto.IntegracaoDTO;
import com.ctsousa.econcilia.repository.IntegracaoRepository;
import com.ctsousa.econcilia.service.IntegracaoService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IntegracaoServiceImpl implements IntegracaoService {

//    private static final String UNDEFINED = "undefined";

    private final IntegracaoRepository integracaoRepository;

    private final IntegracaoMapper integracaoMapper;

    private final IfoodGateway ifoodGateway;

//    private final VendaMapper vendaMapper;
//
//    private final AjusteVendaMapper ajusteVendaMapper;
//
//    private final PagamentoMapper pagamentoMapper;
//
//    private final CancelamentoMapper cancelamentoMapper;
//
//    private final CobrancaCanceladaMapper cobrancaCanceladaMapper;
//
//    private final ImpostoRendaMapper impostoRendaMapper;
//
//    private final OcorrenciaMapper ocorrenciaMapper;
//
//    private final RegistroContaReceberMapper registroContaReceberMapper;
//
//    private final TaxaManutencaoMapper taxaManutencaoMapper;

    public IntegracaoServiceImpl(IntegracaoRepository integracaoRepository, IntegracaoMapper integracaoMapper, IfoodGateway ifoodGateway) {
        this.integracaoRepository = integracaoRepository;
        this.integracaoMapper = integracaoMapper;
        this.ifoodGateway = ifoodGateway;
    }


//    @Deprecated(forRemoval = true)
//    @Override
//    public List<TaxaManutencao> pesquisarTaxasManutencao(String codigoIntegracao, LocalDate dtInicial, LocalDate dtFinal) {
//
//        validaPeriodoMaior90Dias(dtInicial, dtFinal);
//
//        List<MaintenanceFee> maintenanceFees = ifoodGateway.findMaintenanceFees(codigoIntegracao, dtInicial, dtFinal);
//
//        if (maintenanceFees.isEmpty()) {
//            return new ArrayList<>();
//        }
//
//        return taxaManutencaoMapper.paraLista(maintenanceFees);
//    }
//
//    @Deprecated(forRemoval = true)
//    @Override
//    public List<ImpostoRenda> pesquisarImpostoRenda(String codigoIntegracao, LocalDate dtInicial, LocalDate dtFinal) {
//
//        validaPeriodoMaior90Dias(dtInicial, dtFinal);
//
//        List<IncomeTaxe> incomeTaxes = ifoodGateway.findIncomeTaxes(codigoIntegracao, dtInicial, dtFinal);
//
//        if (incomeTaxes.isEmpty()) {
//            return new ArrayList<>();
//        }
//
//        return impostoRendaMapper.paraLista(incomeTaxes);
//    }
//
//    @Deprecated(forRemoval = true)
//    @Override
//    public List<RegistroContaReceber> pesquisarRegistroContaReceber(String codigoIntegracao, LocalDate dtInicial, LocalDate dtFinal) {
//
//        validaPeriodoMaior90Dias(dtInicial, dtFinal);
//
//        List<ReceivableRecord> receivableRecords = ifoodGateway.findReceivables(codigoIntegracao, dtInicial, dtFinal);
//
//        if (receivableRecords.isEmpty()) {
//            return new ArrayList<>();
//        }
//
//        return registroContaReceberMapper.paraLista(receivableRecords);
//    }
//
//    @Deprecated(forRemoval = true)
//    @Override
//    public List<Ocorrencia> pesquisarOcorrencias(String codigoIntegracao, LocalDate dtInicial, LocalDate dtFinal) {
//
//        validaPeriodoMaior90Dias(dtInicial, dtFinal);
//
//        List<Occurrence> occurrences = ifoodGateway.findOccurences(codigoIntegracao, dtInicial, dtFinal);
//
//        if (occurrences.isEmpty()) {
//            return new ArrayList<>();
//        }
//
//        return ocorrenciaMapper.paraLista(occurrences);
//    }
//
//    @Deprecated(forRemoval = true)
//    @Override
//    public List<CobrancaCancelada> pesquisarCobrancaCanceladas(String codigoIntegracao, LocalDate dtInicial, LocalDate dtFinal) {
//
//        validaPeriodoMaior90Dias(dtInicial, dtFinal);
//
//        List<ChargeCancellation> chargeCancellations = ifoodGateway.findChargeCancellationBy(codigoIntegracao, dtInicial, dtFinal);
//
//        if (chargeCancellations.isEmpty()) {
//            return new ArrayList<>();
//        }
//
//        return cobrancaCanceladaMapper.paraLista(chargeCancellations);
//    }
//
//    @Deprecated(forRemoval = true)
//    @Override
//    public List<Cancelamento> pesquisarCancelamentos(String codigoIntegracao, String periodoId) {
//        List<Cancellation> cancellations = ifoodGateway.findCancellationBy(codigoIntegracao, periodoId);
//
//        if (cancellations.isEmpty()) {
//            return new ArrayList<>();
//        }
//
//        return cancelamentoMapper.paraLista(cancellations);
//    }
//
//    @Deprecated(forRemoval = true)
//    @Override
//    public List<Venda> pesquisarVendasIfood(String codigoIntegracao, String metodoPagamento, String bandeira, String tipoRecebimento, LocalDate dtInicial, LocalDate dtFinal) {
//
//        validaPeriodoMaior90Dias(dtInicial, dtFinal);
//
//        List<Sale> sales = ifoodGateway.findSalesBy(codigoIntegracao, dtInicial, dtFinal);
//
//        if (sales.isEmpty()) {
//            return new ArrayList<>();
//        }
//
//        List<Venda> vendas = vendaMapper.paraLista(sales);
//        return filtrarVendas(vendas, metodoPagamento, bandeira, tipoRecebimento);
//    }
//
//    @Deprecated(forRemoval = true)
//    @Override
//    public List<AjusteVenda> pesquisarAjusteVendasIfood(String codigoIntegracao, LocalDate dtInicial, LocalDate dtFinal) {
//        validaPeriodoMaior90Dias(dtInicial, dtFinal);
//
//        List<SaleAdjustment> saleAdjustments = ifoodGateway.findSaleAdjustmentBy(codigoIntegracao, dtInicial, dtFinal);
//
//        if (saleAdjustments.isEmpty()) {
//            return new ArrayList<>();
//        }
//
//        return ajusteVendaMapper.paraLista(saleAdjustments);
//    }
//
//    @Deprecated(forRemoval = true)
//    @Override
//    public List<Pagamento> pesquisarPagamentos(String codigoIntegracao, LocalDate dtInicial, LocalDate dtFinal) {
//        validaPeriodoMaior90Dias(dtInicial, dtFinal);
//
//        List<Payment> payments = ifoodGateway.findPaymentBy(codigoIntegracao, dtInicial, dtFinal);
//
//        if (payments.isEmpty()) {
//            return new ArrayList<>();
//        }
//
//        return pagamentoMapper.paraLista(payments);
//    }

    @Override
    public Integracao salvar(Integracao integracao) {
        var codigoIntegracao = integracao.getCodigoIntegracao().trim();

        ifoodGateway.verifyMerchantById(codigoIntegracao);

        if (integracaoRepository.existsByOperadoraAndCodigoIntegracao(integracao.getOperadora(), codigoIntegracao)) {
            throw new NotificacaoException("Já existe uma integração..:: " + codigoIntegracao + ", com a operadora..:: " + integracao.getOperadora().getDescricao());
        }

        return integracaoRepository.save(integracao);
    }

    @Override
    public List<Integracao> pesquisar(final Long empresaId, final Long operadoraId, final String codigoIntegracao) {

        List<Integracao> integracoes;

        var empresa = new Empresa(empresaId);
        var operadora = new Operadora(operadoraId);

        if (codigoIntegracao != null && !codigoIntegracao.isEmpty()) {
            return List.of(pesquisarPorCodigoIntegracao(codigoIntegracao));
        }

        if (empresaId != null && operadoraId != null) {
            integracoes = integracaoRepository.findByEmpresaAndOperadora(empresa, operadora);
        } else if (empresaId != null) {
            integracoes = integracaoRepository.findByEmpresa(empresa);
        } else if (operadoraId != null) {
            integracoes = integracaoRepository.findByOperadora(operadora);
        } else {
            integracoes = integracaoRepository.findAll();
        }

        return integracoes;
    }

    @Override
    public Integracao pesquisar(Empresa empresa, Operadora operadora) {
        List<Integracao> integracoes = this.pesquisar(empresa.getId(), operadora.getId(), null);

        if (integracoes.isEmpty()) {
            throw new NotificacaoException("Não existe integração para a empresa com id ::: " + empresa.getId());
        }

        if (integracoes.size() > 1) {
            throw new NotificacaoException("Existe mais de uma integração para a empresa com id ::: " + empresa.getId());
        }

        return integracoes.get(0);
    }

    @Override
    public void deletar(Long id) {
        var integracao = pesquisarPorId(id);
        integracaoRepository.delete(integracao);
    }

    @Override
    public Integracao atualizar(Long id, IntegracaoDTO integracaoDTO) {
        pesquisarPorId(id);

        integracaoDTO.setId(id);
        Integracao integracao = integracaoMapper.paraEntidade(integracaoDTO);
        integracaoRepository.save(integracao);

        return integracao;
    }

    @Override
    public Integracao pesquisarPorId(Long id) {
        return integracaoRepository.porID(id)
                .orElseThrow(() -> new NotificacaoException(String.format("Integracao com id %d não encontrado", id)));
    }

    @Override
    public Integracao pesquisarPorCodigoIntegracao(String codigoIntegracao) {
        return integracaoRepository.findByCodigoIntegracao(codigoIntegracao)
                .orElseThrow(() -> new NotificacaoException(String.format("Integração com código integração %s não encontrado", codigoIntegracao)));
    }

//    private List<Venda> filtrarVendas(final List<Venda> vendas, final String metodoPagamento, final String bandeira, final String tipoRecebimento) {
//        return new VendaFiltro(vendas, bandeira, metodoPagamento, tipoRecebimento)
//                .porBandeira()
//                .porMetodoPagamento()
//                .porMetodoPagamentoBandeira()
//                .porTipoRecebimento()
//                .getVendasFiltradas();
//    }
//
//    private void validaPeriodoMaior90Dias(final LocalDate dtInicial, final LocalDate dtFinal) {
//        long dias = ChronoUnit.DAYS.between(dtInicial, dtFinal);
//
//        if (dias > 90) {
//            throw new NotificacaoException("O período não pode ser maior que 90 dias");
//        }
//    }
}

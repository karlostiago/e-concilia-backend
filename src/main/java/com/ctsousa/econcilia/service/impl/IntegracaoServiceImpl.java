package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.integration.ifood.IfoodGateway;
import com.ctsousa.econcilia.integration.ifood.entity.Payment;
import com.ctsousa.econcilia.integration.ifood.entity.Sale;
import com.ctsousa.econcilia.integration.ifood.entity.SaleAdjustment;
import com.ctsousa.econcilia.mapper.AjusteVendaMapper;
import com.ctsousa.econcilia.mapper.IntegracaoMapper;
import com.ctsousa.econcilia.mapper.PagamentoMapper;
import com.ctsousa.econcilia.mapper.VendaMapper;
import com.ctsousa.econcilia.model.*;
import com.ctsousa.econcilia.model.dto.IntegracaoDTO;
import com.ctsousa.econcilia.repository.IntegracaoRepository;
import com.ctsousa.econcilia.service.IntegracaoService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Component
public class IntegracaoServiceImpl implements IntegracaoService {

    private static final String UNDEFINED = "undefined";

    private final IntegracaoRepository integracaoRepository;

    private final IntegracaoMapper integracaoMapper;

    private final IfoodGateway ifoodGateway;

    private final VendaMapper vendaMapper;

    private final AjusteVendaMapper ajusteVendaMapper;

    private final PagamentoMapper pagamentoMapper;

    public IntegracaoServiceImpl(IntegracaoRepository integracaoRepository, IntegracaoMapper integracaoMapper, IfoodGateway ifoodGateway, VendaMapper vendaMapper, AjusteVendaMapper ajusteVendaMapper, PagamentoMapper pagamentoMapper) {
        this.integracaoRepository = integracaoRepository;
        this.integracaoMapper = integracaoMapper;
        this.ifoodGateway = ifoodGateway;
        this.vendaMapper = vendaMapper;
        this.ajusteVendaMapper = ajusteVendaMapper;
        this.pagamentoMapper = pagamentoMapper;
    }

    @Override
    public List<Venda> pesquisarVendasIfood(String codigoIntegracao, String metodoPagamento, String bandeira, LocalDate dtInicial, LocalDate dtFinal) {

        validaPeriodoMaior90Dias(dtInicial, dtFinal);

        List<Sale> sales = ifoodGateway.findSalesBy(codigoIntegracao, dtInicial, dtFinal);

        if (sales.isEmpty()) {
            return new ArrayList<>();
        }

        List<Venda> vendas = vendaMapper.paraLista(sales);
        return filtrarVendas(vendas, metodoPagamento, bandeira);
    }

    @Override
    public List<AjusteVenda> pesquisarAjusteVendasIfood(String codigoIntegracao, LocalDate dtInicial, LocalDate dtFinal) {
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
        }
        else if (empresaId != null) {
            integracoes = integracaoRepository.findByEmpresa(empresa);
        }
        else if (operadoraId != null) {
            integracoes = integracaoRepository.findByOperadora(operadora);
        }
        else {
            integracoes = integracaoRepository.findAll();
        }

        return integracoes;
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

    private List<Venda> filtrarVendas(final List<Venda> vendas, final String metodoPagamento, final String bandeira) {
        List<Venda> vendasFiltradas = new ArrayList<>(vendas);

        if (metodoPagamento != null && !metodoPagamento.isEmpty() && !UNDEFINED.equalsIgnoreCase(metodoPagamento)
            && bandeira != null && !bandeira.isEmpty() && !UNDEFINED.equalsIgnoreCase(bandeira)) {
            vendasFiltradas = vendas.stream().filter(venda -> {
                        var mPagamento = venda.getPagamento().getMetodo().toUpperCase();
                        var descBandeira = venda.getPagamento().getBandeira().toUpperCase();
                        return descBandeira.contains(bandeira.toUpperCase())
                                && mPagamento.equalsIgnoreCase(metodoPagamento);
                    })
                    .toList();
        }
        else if (metodoPagamento != null && !metodoPagamento.isEmpty() && !UNDEFINED.equalsIgnoreCase(metodoPagamento)) {
            vendasFiltradas = vendas.stream().filter(venda -> venda.getPagamento().getMetodo().equalsIgnoreCase(metodoPagamento))
                    .toList();
        }
        else if (bandeira != null && !bandeira.isEmpty() && !UNDEFINED.equalsIgnoreCase(bandeira)) {
            vendasFiltradas = vendas.stream().filter(venda -> {
                        var descBandeira = venda.getPagamento().getBandeira().toUpperCase();
                        return descBandeira.contains(bandeira.toUpperCase());
                    })
                    .toList();
        }

        return vendasFiltradas;
    }

    private void validaPeriodoMaior90Dias(final LocalDate dtInicial, final LocalDate dtFinal) {
        long dias = ChronoUnit.DAYS.between(dtInicial, dtFinal);

        if (dias > 90) {
            throw new NotificacaoException("O período não pode ser maior que 90 dias");
        }
    }
}

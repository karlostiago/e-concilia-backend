package com.ctsousa.econcilia.mapper.impl;

import com.ctsousa.econcilia.integration.ifood.entity.Sale;
import com.ctsousa.econcilia.mapper.ColecaoMapper;
import com.ctsousa.econcilia.mapper.DtoMapper;
import com.ctsousa.econcilia.model.Cobranca;
import com.ctsousa.econcilia.model.Pagamento;
import com.ctsousa.econcilia.model.Venda;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VendaMapper implements DtoMapper<Sale, Venda>, ColecaoMapper<Sale, Venda> {

    @Override
    public List<Venda> paraLista(List<Sale> sales) {
        return sales.stream()
                .map(this::paraDTO)
                .toList();
    }

    @Override
    public Venda paraDTO(Sale sale) {
        Venda venda = new Venda();
        venda.setPeriodoId(sale.getPeriodId());
        venda.setDataPedido(sale.getOrderDate());
        venda.setPedidoId(sale.getOrderId());
        venda.setModeloNegocio(sale.getBusinessModelOrder());
        venda.setRazaoSocial(sale.getCompanyName());
        venda.setUltimaDataProcessamento(sale.getLastProcessingDate());
        venda.setNumeroDocumento(sale.getDocumentNumber());
        venda.setPagamento(getPagamento(sale));
        venda.setCobranca(getCobranca(sale));
        return venda;
    }

    private Pagamento getPagamento(final Sale sale) {
        Pagamento pagamento = new Pagamento();
        pagamento.setBandeira(sale.getPayment().getBrand());
        pagamento.setTipo(sale.getPayment().getType());
        pagamento.setNsu(sale.getPayment().getNsu());
        pagamento.setMetodo(sale.getPayment().getMethod());
        pagamento.setResponsavel(sale.getPayment().getLiability());
        pagamento.setNumeroCartao(sale.getPayment().getCardNumber());
        return pagamento;
    }

    private Cobranca getCobranca(final Sale sale) {
        Cobranca cobranca = new Cobranca();
        cobranca.setValorBruto(sale.getBilling().getGmv());
        cobranca.setValorParcial(sale.getBilling().getTotalBag());
        cobranca.setTaxaEntrega(sale.getBilling().getDeliveryFee());
        cobranca.setBeneficioOperadora(sale.getBilling().getBenefitIfood());
        cobranca.setBeneficioComercio(sale.getBilling().getBenefitMerchant());
        cobranca.setComissao(sale.getBilling().getCommission());
        cobranca.setTaxaAdquirente(sale.getBilling().getAcquirerFee());
        cobranca.setComissaoEntrega(sale.getBilling().getDeliveryCommission());
        cobranca.setTaxaComissao(sale.getBilling().getCommissionRate());
        cobranca.setTaxaComissaoAdquirente(sale.getBilling().getAcquirerFeeRate());
        cobranca.setTotalDebito(sale.getBilling().getTotalDebit());
        cobranca.setTotalCredito(sale.getBilling().getTotalCredit());
        cobranca.setValorTaxaAntecipacao(sale.getBilling().getAnticipationFee());
        cobranca.setTaxaAntecipacao(sale.getBilling().getAnticipationFeeRate());
        cobranca.setTaxaServico(sale.getBilling().getSmallOrderFee());
        return cobranca;
    }
}

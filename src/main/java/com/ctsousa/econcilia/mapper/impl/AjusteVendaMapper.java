package com.ctsousa.econcilia.mapper.impl;

import com.ctsousa.econcilia.annotation.ExcludedCoverage;
import com.ctsousa.econcilia.integration.ifood.entity.SaleAdjustment;
import com.ctsousa.econcilia.mapper.ColecaoMapper;
import com.ctsousa.econcilia.mapper.DtoMapper;
import com.ctsousa.econcilia.model.AjusteVenda;
import com.ctsousa.econcilia.model.Cobranca;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ExcludedCoverage
public class AjusteVendaMapper implements DtoMapper<SaleAdjustment, AjusteVenda>, ColecaoMapper<SaleAdjustment, AjusteVenda> {

    @Override
    public List<AjusteVenda> paraLista(List<SaleAdjustment> salesAdjustment) {
        return salesAdjustment.stream()
                .map(this::paraDTO)
                .toList();
    }

    @Override
    public AjusteVenda paraDTO(SaleAdjustment saleAdjustment) {
        AjusteVenda ajusteVenda = new AjusteVenda();
        ajusteVenda.setPeriodoId(saleAdjustment.getPeriodId());
        ajusteVenda.setPedidoId(saleAdjustment.getOrderId());
        ajusteVenda.setPedidoCobrancaId(saleAdjustment.getBilledOrderId());
        ajusteVenda.setDataPedido(saleAdjustment.getOrderDate());
        ajusteVenda.setDataPedidoAtualizado(saleAdjustment.getOrderDateUpdate());
        ajusteVenda.setValorAjuste(saleAdjustment.getAdjustmentAmount());
        ajusteVenda.setNumeroDocumento(saleAdjustment.getDocumentNumber());
        ajusteVenda.setCobranca(getCobranca(saleAdjustment));
        return ajusteVenda;
    }

    private Cobranca getCobranca(final SaleAdjustment saleAdjustment) {
        Cobranca cobranca = new Cobranca();
        cobranca.setValorBruto(saleAdjustment.getBilledOrderUpdate().getGmv());
        cobranca.setValorParcial(saleAdjustment.getBilledOrderUpdate().getTotalBag());
        cobranca.setTaxaEntrega(saleAdjustment.getBilledOrderUpdate().getDeliveryFee());
        cobranca.setBeneficioOperadora(saleAdjustment.getBilledOrderUpdate().getBenefitIfood());
        cobranca.setBeneficioComercio(saleAdjustment.getBilledOrderUpdate().getBenefitMerchant());
        cobranca.setComissao(saleAdjustment.getBilledOrderUpdate().getCommission());
        cobranca.setTaxaAdquirente(saleAdjustment.getBilledOrderUpdate().getAcquirerFee());
        cobranca.setComissaoEntrega(saleAdjustment.getBilledOrderUpdate().getDeliveryCommission());
        cobranca.setTaxaComissao(saleAdjustment.getBilledOrderUpdate().getCommissionRate());
        cobranca.setTaxaComissaoAdquirente(saleAdjustment.getBilledOrderUpdate().getAcquirerFeeRate());
        cobranca.setTotalDebito(saleAdjustment.getBilledOrderUpdate().getTotalDebit());
        cobranca.setTotalCredito(saleAdjustment.getBilledOrderUpdate().getTotalCredit());
        cobranca.setValorTaxaAntecipacao(saleAdjustment.getBilledOrderUpdate().getAnticipationFee());
        cobranca.setTaxaAntecipacao(saleAdjustment.getBilledOrderUpdate().getAnticipationFeeRate());
        cobranca.setTaxaServico(saleAdjustment.getBilledOrderUpdate().getSmallOrderFee());
        return cobranca;
    }
}

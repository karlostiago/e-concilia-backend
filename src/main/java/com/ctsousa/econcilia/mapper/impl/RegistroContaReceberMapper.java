package com.ctsousa.econcilia.mapper.impl;

import com.ctsousa.econcilia.annotation.ExcludedCoverage;
import com.ctsousa.econcilia.integration.ifood.entity.PaymentSplit;
import com.ctsousa.econcilia.integration.ifood.entity.ReceivableRecord;
import com.ctsousa.econcilia.mapper.ColecaoMapper;
import com.ctsousa.econcilia.mapper.DtoMapper;
import com.ctsousa.econcilia.model.DivisaoPagamento;
import com.ctsousa.econcilia.model.RegistroContaReceber;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ExcludedCoverage
public class RegistroContaReceberMapper implements DtoMapper<ReceivableRecord, RegistroContaReceber>, ColecaoMapper<ReceivableRecord, RegistroContaReceber> {

    @Override
    public List<RegistroContaReceber> paraLista(List<ReceivableRecord> receivableRecords) {
        return receivableRecords.stream()
                .map(this::paraDTO)
                .toList();
    }

    @Override
    public RegistroContaReceber paraDTO(ReceivableRecord receivableRecord) {
        RegistroContaReceber registroContaReceber = new RegistroContaReceber();
        registroContaReceber.setId(receivableRecord.getId());
        registroContaReceber.setOrigemPagamentoId(receivableRecord.getOriginPaymentId());
        registroContaReceber.setPagamentos(divisaoPagamentos(receivableRecord.getPaymentSplit()));
        return registroContaReceber;
    }

    private DivisaoPagamento getDivisaoPagamento(PaymentSplit paymentSplit) {
        DivisaoPagamento divisaoPagamento = new DivisaoPagamento();
        divisaoPagamento.setData(paymentSplit.getDate());
        divisaoPagamento.setValorTotal(paymentSplit.getTotalAmount());
        divisaoPagamento.setId(paymentSplit.getId());
        return divisaoPagamento;
    }

    private List<DivisaoPagamento> divisaoPagamentos(List<PaymentSplit> paymentSplits) {
        return paymentSplits.stream()
                .map(this::getDivisaoPagamento)
                .toList();
    }
}

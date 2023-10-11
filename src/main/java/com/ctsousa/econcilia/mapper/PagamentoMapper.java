package com.ctsousa.econcilia.mapper;

import com.ctsousa.econcilia.integration.ifood.entity.Payment;
import com.ctsousa.econcilia.model.Pagamento;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PagamentoMapper implements DtoMapper<Payment, Pagamento>, ColecaoMapper<Payment, Pagamento> {

    @Override
    public List<Pagamento> paraLista(List<Payment> payments) {
        return payments.stream()
                .map(this::paraDTO)
                .toList();
    }

    @Override
    public Pagamento paraDTO(Payment payment) {
        Pagamento pagamento = new Pagamento();
        pagamento.setPeriodoId(payment.getPeriodId());
        pagamento.setStatus(payment.getStatus());
        pagamento.setTipo(payment.getType());
        pagamento.setDataConfirmacaoPagamento(payment.getConfirmedPaymentDate());
        pagamento.setCodigoTransacao(payment.getTransactionCode());
        pagamento.setValorTotal(payment.getTotalAmount());
        pagamento.setDataExecucaoEsperada(payment.getExpectedExecutionDate());
        pagamento.setProximaDataExecucao(payment.getNextExecutionDate());
        return pagamento;
    }
}

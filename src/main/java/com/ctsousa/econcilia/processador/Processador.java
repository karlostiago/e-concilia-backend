package com.ctsousa.econcilia.processador;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public abstract class Processador implements Executor {

    protected BigDecimal valorTotalBruto;

    protected BigDecimal valorTotalTicketMedio;

    protected BigDecimal valorTotalCancelado;

    protected BigDecimal valorTotalRecebido;

    protected BigDecimal valorTotalComissaoTransacaoPagamento;

    protected BigDecimal valorTotalComissao;

    protected BigDecimal valorTotalTaxaEntrega;

    protected BigDecimal valorTotalPagar;

    protected BigDecimal valorTotalPromocao;

    protected Integer quantidade;

}

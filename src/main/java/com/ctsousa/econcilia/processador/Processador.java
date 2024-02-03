package com.ctsousa.econcilia.processador;

import com.ctsousa.econcilia.model.Venda;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public abstract class Processador implements Executor {

    protected BigDecimal valorTotalBruto;

    protected BigDecimal valorTotalTicketMedio;

    protected BigDecimal valorTotalCancelado;

    protected BigDecimal valorTotalRecebido;

    protected BigDecimal valorTotalComissaoTransacaoPagamento;

    protected BigDecimal valorTotalComissao;

    protected BigDecimal valorTotalTaxaEntrega;

    protected BigDecimal valorTotalRepasse;

    protected BigDecimal valorTotalPromocao;

    protected Integer quantidade;

    protected List<Venda> vendas;
}

package com.ctsousa.econcilia.processor;

import com.ctsousa.econcilia.model.Venda;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
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

    protected BigDecimal valorTotalPedido;

    protected BigDecimal valorTotalLiquido;

    protected Integer quantidade;

    protected List<Venda> vendas;

    protected void reiniciar() {
        this.valorTotalBruto = BigDecimal.valueOf(0D);
        this.valorTotalTicketMedio = BigDecimal.valueOf(0D);
        this.valorTotalCancelado = BigDecimal.valueOf(0D);
        this.valorTotalRecebido = BigDecimal.valueOf(0D);
        this.valorTotalComissaoTransacaoPagamento = BigDecimal.valueOf(0D);
        this.valorTotalComissao = BigDecimal.valueOf(0D);
        this.valorTotalTaxaEntrega = BigDecimal.valueOf(0D);
        this.valorTotalRepasse = BigDecimal.valueOf(0D);
        this.valorTotalPromocao = BigDecimal.valueOf(0D);
        this.valorTotalPedido = BigDecimal.valueOf(0D);
        this.valorTotalLiquido = BigDecimal.valueOf(0D);
        this.vendas = new ArrayList<>();
        this.quantidade = 0;
    }
}

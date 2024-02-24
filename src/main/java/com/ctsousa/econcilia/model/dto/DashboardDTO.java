package com.ctsousa.econcilia.model.dto;

import com.ctsousa.econcilia.annotation.ExcludedCoverage;
import com.ctsousa.econcilia.model.Venda;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Getter
@Setter
@ExcludedCoverage
public class DashboardDTO {

    private BigDecimal valorBrutoVendas;

    private BigInteger quantidadeVendas;

    private BigDecimal ticketMedio;

    private BigDecimal valorCancelamento;

    private BigDecimal valorRecebidoLoja;

    private BigDecimal valorTaxaEntrega;

    private BigDecimal valorEmRepasse;

    private BigDecimal valorComissao;

    private BigDecimal valorPromocao;

    private BigDecimal valorComissaoTransacao;

    private List<Venda> vendas;

    public DashboardDTO() {
        this.setValorBrutoVendas(BigDecimal.valueOf(0D));
        this.setQuantidadeVendas(BigInteger.valueOf(0));
        this.setTicketMedio(BigDecimal.valueOf(0D));
        this.setValorCancelamento(BigDecimal.valueOf(0D));
        this.setValorRecebidoLoja(BigDecimal.valueOf(0D));
        this.setValorTaxaEntrega(BigDecimal.valueOf(0D));
        this.setValorEmRepasse(BigDecimal.valueOf(0D));
        this.setValorComissao(BigDecimal.valueOf(0D));
        this.setValorPromocao(BigDecimal.valueOf(0D));
        this.setValorComissaoTransacao(BigDecimal.valueOf(0D));
    }
}

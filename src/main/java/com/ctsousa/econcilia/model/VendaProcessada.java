package com.ctsousa.econcilia.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.BigInteger;

@Builder
@Getter
public class VendaProcessada {

    private BigDecimal totalBruto;

    private BigDecimal totalPedido;

    private BigDecimal totalLiquido;

    private BigDecimal totalCancelado;

    private BigDecimal totalTaxaEntrega;

    private BigDecimal totalDesconto;

    private BigDecimal totalTicketMedio;

    private BigDecimal totalComissao;

    private BigInteger quantidade;

    private BigDecimal totalTaxas;

    private BigDecimal taxaMedia;

    private BigDecimal totalRepasse;

    ///
    private BigDecimal totalComissaoTransacaoPagamento;

    private BigDecimal totalComissaoOperadora;

    private BigDecimal totalPromocaoLoja;

    private BigDecimal totalRecebidoLoja;

    private BigDecimal totalRecebidoOperadora;
}

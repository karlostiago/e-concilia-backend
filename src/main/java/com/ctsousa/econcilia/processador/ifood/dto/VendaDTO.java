package com.ctsousa.econcilia.processador.ifood.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.BigInteger;

@Builder
@Getter
public class VendaDTO {

    private BigDecimal totalBruto;

    private BigDecimal totalCancelado;

    private BigDecimal totalTaxaEntrega;

    private BigDecimal totalTicketMedio;

    private BigDecimal totalComissaoTransacaoPagamento;

    private BigDecimal totalComissao;

    private BigInteger quantidade;

    private BigDecimal totalRepasse;

    private BigDecimal totalPromocao;

    private BigDecimal totalRecebidoLoja;
}

package com.ctsousa.econcilia.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@Setter
@Builder
public class DashboardDTO {

    private BigDecimal valorBrutoVendas;

    private BigInteger quantidadeVendas;

    private BigDecimal ticketMedio;

    private BigDecimal valorCancelamento;

    private BigDecimal valorRecebidoLoja;

    private BigDecimal valorIncentivoPromocionalLoja;

    private BigDecimal valorIncentivoPromocionalOperadora;

    private BigDecimal valorTaxaEntrega;

    private BigDecimal valorEmRepasse;

    /// remover
    private BigDecimal valorTaxas;

    private BigDecimal taxaMedia;

    private BigDecimal valorComissao;

    private BigDecimal valorDesconto;
}

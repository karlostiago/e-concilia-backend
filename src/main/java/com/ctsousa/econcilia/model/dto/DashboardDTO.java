package com.ctsousa.econcilia.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@Setter
public class DashboardDTO {

    private BigDecimal valorBrutoVendas;

    private BigInteger quantidadeVendas;

    private BigDecimal ticketMedio;

    private BigDecimal valorCancelamento;

    private BigDecimal valorTaxas;

    private BigDecimal taxaMedia;

    private BigDecimal valorRecebido;

    private BigDecimal valorReceber;
}

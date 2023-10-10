package com.ctsousa.econcilia.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TotalizadorDTO {

    private BigDecimal totalValorPedido = new BigDecimal("0.0");

    private BigDecimal totalTaxaEntrega = new BigDecimal("0.0");;

    private BigDecimal totalTaxaAquisicaoPraticada = new BigDecimal("0.0");;

    private BigDecimal totalTaxaAquisicaoAplicada = new BigDecimal("0.0");;

    private BigDecimal totalDiferenca = new BigDecimal("0.0");;
}

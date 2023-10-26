package com.ctsousa.econcilia.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TotalizadorDTO {

    private BigDecimal totalValorBruto = BigDecimal.valueOf(0D);

    private BigDecimal totalValorCancelado = BigDecimal.valueOf(0D);

    private BigDecimal totalValorPedido = BigDecimal.valueOf(0D);

    private BigDecimal totalValorLiquido = BigDecimal.valueOf(0D);

    @Deprecated
    private BigDecimal totalTaxaEntrega = new BigDecimal("0.0");

    @Deprecated
    private BigDecimal totalTaxaAquisicaoPraticada = new BigDecimal("0.0");

    @Deprecated
    private BigDecimal totalTaxaAquisicaoAplicada = new BigDecimal("0.0");

    @Deprecated
    private BigDecimal totalDiferenca = new BigDecimal("0.0");
}

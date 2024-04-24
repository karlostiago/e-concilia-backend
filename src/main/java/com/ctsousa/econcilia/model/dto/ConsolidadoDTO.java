package com.ctsousa.econcilia.model.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

@Getter
@Builder
public class ConsolidadoDTO {

    private Long empresaId;
    private Long operadoraId;
    private LocalDate periodo;
    private BigDecimal totalBruto;
    private BigDecimal totalLiquido;
    private BigInteger quantidadeVenda;
    private BigDecimal ticketMedio;
    private BigDecimal totalCancelado;
    private BigDecimal totalRecebido;
    private BigDecimal totalTaxaEntrega;
    private BigDecimal totalComissao;
    private BigDecimal totalPromocao;
    private BigDecimal totalTransacaoPagamento;
    private BigDecimal totalTaxaServico;
    private BigDecimal totalRepasse;
    private BigDecimal totalTaxaManutencao;
}


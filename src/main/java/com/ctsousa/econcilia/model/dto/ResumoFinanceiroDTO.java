package com.ctsousa.econcilia.model.dto;

import com.ctsousa.econcilia.Dto;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Dto
public class ResumoFinanceiroDTO {

    private BigDecimal totalCredito = new BigDecimal("0.0");

    private BigDecimal totalDebito = new BigDecimal("0.0");

    private BigDecimal totalVoucher = new BigDecimal("0.0");

    private BigDecimal totalDinheiro = new BigDecimal("0.0");

    private BigDecimal totalPix = new BigDecimal("0.0");

    private BigDecimal totalBankPay = new BigDecimal("0.0");

    private BigDecimal totalOutros = new BigDecimal("0.0");
}

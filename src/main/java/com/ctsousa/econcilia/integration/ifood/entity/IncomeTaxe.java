package com.ctsousa.econcilia.integration.ifood.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class IncomeTaxe {

    private String merchantName;

    private String paymentPlan;

    private String periodId;

    private LocalDate expectedPaymentDate;

    private String transactionId;

    private LocalDate transactionDate;

    private BigDecimal amount;

    private String type;
}

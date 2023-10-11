package com.ctsousa.econcilia.integration.ifood.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class Payment {

    private String id;

    private String periodId;

    private String status;

    private LocalDate expectedExecutionDate;

    private LocalDate nextExecutionDate;

    private LocalDate confirmedPaymentDate;

    private BigDecimal totalAmount;

    private BankAccount bankAccount;

    private String transactionCode;

    private String type;
}

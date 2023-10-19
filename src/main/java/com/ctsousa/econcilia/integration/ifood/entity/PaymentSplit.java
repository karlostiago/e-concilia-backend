package com.ctsousa.econcilia.integration.ifood.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class PaymentSplit {

    private String id;

    private BigDecimal totalAmount;

    private LocalDate date;

    private BankAccount bankAccount;
}

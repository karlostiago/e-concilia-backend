package com.ctsousa.econcilia.integration.ifood.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

@Getter
@Setter
public class MaintenanceFee {

    private String periodId;

    private LocalDate expectedPaymentDate;

    private BigInteger transactionId;

    private LocalDate transactionDate;

    private String type;

    private BigDecimal amount;
}

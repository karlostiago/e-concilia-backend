package com.ctsousa.econcilia.integration.ifood.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class Occurrence {

    private String merchantName;

    private String paymentPlan;

    private String periodId;

    private LocalDate expectedPaymentDate;

    private String transactionId;

    private LocalDate transactionDate;

    private String type;

    private BigDecimal amount;

    private String reason;

    private String description;

    private String documentNumber;

    private String transactionDateTime;

    private String referencePeriodId;
}

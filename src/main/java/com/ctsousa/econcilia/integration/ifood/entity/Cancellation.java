package com.ctsousa.econcilia.integration.ifood.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class Cancellation {

    private String merchantName;

    private String merchantId;

    private String orderId;

    private String periodId;

    private LocalDate expectedPaymentDate;

    private LocalDate cancellationDate;

    private LocalDate orderDate;

    private BigDecimal amount;

    private String liability;

    private String operationType;

    private String displayId;

    private String cancellationCode;

    private String cancellationCodeDescription;
}

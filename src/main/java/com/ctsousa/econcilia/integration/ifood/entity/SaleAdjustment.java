package com.ctsousa.econcilia.integration.ifood.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
public class SaleAdjustment {

    private String orderId;

    private String billedOrderId;

    private String periodId;

    private String documentNumber;

    private LocalDate orderDate;

    private LocalDate orderDateUpdate;

    private BigDecimal adjustmentAmount;

    private LocalDate expectedPaymentDate;

    private Billing billedOrderUpdate;

    private String displayId;

    private String cancellationCode;

    private String cancellationCodeDescription;
}

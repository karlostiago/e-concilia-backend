package com.ctsousa.econcilia.integration.ifood.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Sale {

    private LocalDate orderDate;

    private String orderId;

    private LocalDate lastProcessingDate;

    private String orderStatus;

    private String companyName;

    private String documentNumber;

    private String businessModelOrder;

    private SalePayment payment;

    private Billing billing;
}

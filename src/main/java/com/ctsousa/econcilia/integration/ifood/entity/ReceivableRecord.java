package com.ctsousa.econcilia.integration.ifood.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReceivableRecord {

    private String id;

    private String originPaymentId;

    private List<PaymentSplit> paymentSplit;
}

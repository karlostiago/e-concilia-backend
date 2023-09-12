package com.ctsousa.econcilia.integration.ifood.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Billing {

    private BigDecimal gmv;

    private BigDecimal totalBag;

    private BigDecimal deliveryFee;

    private BigDecimal benefitIfood;

    private BigDecimal benefitMerchant;

    private BigDecimal commission;

    private BigDecimal acquirerFee;

    private BigDecimal deliveryCommission;

    private BigDecimal commissionRate;

    private BigDecimal acquirerFeeRate;

    private BigDecimal totalDebit;

    private BigDecimal totalCredit;

    private BigDecimal anticipationFee;

    private BigDecimal anticipationFeeRate;

    private BigDecimal smallOrderFee;

    private BigDecimal benefitPaymentCredit;

    private BigDecimal benefitAcquirerFee;
}

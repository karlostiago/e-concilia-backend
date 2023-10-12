package com.ctsousa.econcilia.integration.ifood;

import com.ctsousa.econcilia.integration.ifood.entity.*;

import java.time.LocalDate;
import java.util.List;

public interface IfoodGateway {

    void verifyMerchantById(final String uuid);

    List<Sale> findSalesBy(final String uuid, LocalDate startDate, LocalDate endDate);

    List<SaleAdjustment> findSaleAdjustmentBy(final String uuid, LocalDate startDate, LocalDate endDate);

    List<Payment> findPaymentBy(final String uuid, LocalDate startDate, LocalDate endDate);

    List<Cancellation> findCancellationBy(final String uuid, LocalDate startDate, LocalDate endDate);

    List<ChargeCancellation> findChargeCancellationBy(final String uuid, LocalDate startDate, LocalDate endDate);
}

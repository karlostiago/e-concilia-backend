package com.ctsousa.econcilia.integration.ifood.service;

import com.ctsousa.econcilia.integration.ifood.entity.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface FinancialService {

    List<Sale> sales(String token, String uuid, LocalDate startDate, LocalDate endDate);

    List<SaleAdjustment> salesAdjustments(final String token, final String uuid, final LocalDate startDate, final LocalDate endDate);

    List<Payment> payments(final String token, final String uuid, final LocalDate startDate, final LocalDate endDate);

    List<Cancellation> cancellations(final String token, final String uuid, final String periodId);

    List<Period> periods(final String token, final String uuid, final LocalDate competence);

    List<ChargeCancellation> chargeCancellations(final String token, final String uuid, final LocalDate startDate, final LocalDate endDate);

    List<IncomeTaxe> incomeTaxes(final String token, final String uuid, final LocalDate startDate, final LocalDate endDate);

    List<MaintenanceFee> maintenanceFees(final String token, final String uuid, final LocalDate startDate, final LocalDate endDate);

    List<Occurrence> occurrences(final String token, final String uuid, final LocalDate startDate, final LocalDate endDate);

    List<ReceivableRecord> receivableRecords(final String token, final String uuid, final LocalDate startDate, final LocalDate endDate);
}

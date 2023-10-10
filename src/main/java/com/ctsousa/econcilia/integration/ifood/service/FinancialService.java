package com.ctsousa.econcilia.integration.ifood.service;

import com.ctsousa.econcilia.integration.ifood.entity.Sale;
import com.ctsousa.econcilia.integration.ifood.entity.SaleAdjustment;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface FinancialService {

    List<Sale> sales(String token, String uuid, LocalDate startDate, LocalDate endDate);

    List<SaleAdjustment> salesAdjustments(final String token, final String uuid, final LocalDate startDate, final LocalDate endDate);
}

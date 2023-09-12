package com.ctsousa.econcilia.integration.ifood.service;

import com.ctsousa.econcilia.integration.ifood.entity.Sale;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface FinancialService {

    List<Sale> sales(String token, String uuid, LocalDate startDate, LocalDate endDate);
}

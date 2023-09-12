package com.ctsousa.econcilia.integration.ifood;

import com.ctsousa.econcilia.integration.ifood.entity.Sale;

import java.time.LocalDate;
import java.util.List;

public interface IfoodGateway {

    void verifyMerchantById(final String uuid);

    List<Sale> findSalesBy(final String uuid, LocalDate startDate, LocalDate endDate);
}

package com.ctsousa.econcilia.integration.ifood.service.impl;

import com.ctsousa.econcilia.integration.ifood.AbstractIfoodService;
import com.ctsousa.econcilia.integration.ifood.entity.Sale;
import com.ctsousa.econcilia.integration.ifood.service.FinancialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
public class FinancialServiceImpl extends AbstractIfoodService implements FinancialService {

    @Override
    public List<Sale> sales(String token, String uuid, LocalDate startDate, LocalDate endDate) {

        log.info("Buscando vendas realizadas no período de {} até {}", startDate, endDate);

        String path = pathBase().concat("/").concat(uuid).concat("/sales?").concat("beginOrderDate=" + startDate)
                .concat("&endOrderDate=" + endDate);

        ParameterizedTypeReference<List<Sale>> responseType = new ParameterizedTypeReference<>() { };

        var response = restTemplate.exchange(
            path,
            HttpMethod.GET,
            new HttpEntity<>(getHttpHeaders(token)),
            responseType
        );

        return response.getBody();
    }

    @Override
    public String pathBase() {
        return "https://merchant-api.ifood.com.br/financial/v2.0/merchants";
    }
}

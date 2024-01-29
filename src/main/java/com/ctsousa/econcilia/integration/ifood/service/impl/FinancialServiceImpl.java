package com.ctsousa.econcilia.integration.ifood.service.impl;

import com.ctsousa.econcilia.integration.ifood.AbstractIfoodService;
import com.ctsousa.econcilia.integration.ifood.entity.*;
import com.ctsousa.econcilia.integration.ifood.service.FinancialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
public class FinancialServiceImpl extends AbstractIfoodService implements FinancialService {

    private static final String TRANSACTION_DATE_BEGIN = "transactionDateBegin";

    private static final String TRANSACTION_DATE_END = "transactionDateEnd";

    @Override
    public List<ReceivableRecord> receivableRecords(String token, String uuid, LocalDate startDate, LocalDate endDate) {

        log.info("Buscando registros de contas a receber no período de {} até {}", startDate, endDate);

        String path = pathBase().concat("/").concat(uuid).concat("/receivableRecords?").concat("beginReceivableDate=" + startDate)
                .concat("&endReceivableDate=" + endDate);

        ParameterizedTypeReference<List<ReceivableRecord>> responseType = new ParameterizedTypeReference<>() {
        };

        return requestProcess(path, token, responseType);
    }

    @Override
    public List<Occurrence> occurrences(String token, String uuid, LocalDate startDate, LocalDate endDate) {

        log.info("Buscando ocorrências no período de {} até {}", startDate, endDate);

        String path = pathBase().concat("/").concat(uuid).concat("/occurrences?").concat(TRANSACTION_DATE_BEGIN + "=" + startDate)
                .concat("&" + TRANSACTION_DATE_END + "=" + endDate);

        ParameterizedTypeReference<List<Occurrence>> responseType = new ParameterizedTypeReference<>() {
        };

        return requestProcess(path, token, responseType);
    }

    @Override
    public List<MaintenanceFee> maintenanceFees(String token, String uuid, LocalDate startDate, LocalDate endDate) {

        log.info("Buscando taxa de manutenção no período de {} até {}", startDate, endDate);

        String path = pathBase().concat("/").concat(uuid).concat("/maintenanceFees?").concat(TRANSACTION_DATE_BEGIN + "=" + startDate)
                .concat("&" + TRANSACTION_DATE_END + "=" + endDate);

        ParameterizedTypeReference<List<MaintenanceFee>> responseType = new ParameterizedTypeReference<>() {
        };

        return requestProcess(path, token, responseType);
    }

    @Override
    public List<IncomeTaxe> incomeTaxes(String token, String uuid, LocalDate startDate, LocalDate endDate) {

        log.info("Buscando imposto de renda no período de {} até {}", startDate, endDate);

        String path = pathBase().concat("/").concat(uuid).concat("/incomeTaxes?").concat(TRANSACTION_DATE_BEGIN + "=" + startDate)
                .concat("&" + TRANSACTION_DATE_END + "=" + endDate);

        ParameterizedTypeReference<List<IncomeTaxe>> responseType = new ParameterizedTypeReference<>() {
        };

        return requestProcess(path, token, responseType);
    }

    @Override
    public List<ChargeCancellation> chargeCancellations(String token, String uuid, LocalDate startDate, LocalDate endDate) {

        log.info("Buscando cancelamentos de cobrança no período de {} até {}", startDate, endDate);

        String path = pathBase().concat("/").concat(uuid).concat("/chargeCancellations?").concat(TRANSACTION_DATE_BEGIN + "=" + startDate)
                .concat("&" + TRANSACTION_DATE_END + "=" + endDate);

        ParameterizedTypeReference<List<ChargeCancellation>> responseType = new ParameterizedTypeReference<>() {
        };

        return requestProcess(path, token, responseType);
    }

    @Override
    public List<Period> periods(String token, String uuid, LocalDate competence) {

        log.info("Buscando os períodos da competência {}", competence);

        String path = pathBase().concat("/").concat(uuid).concat("/periods?").concat("competence=" + competence);

        ParameterizedTypeReference<List<Period>> responseType = new ParameterizedTypeReference<>() {
        };

        return requestProcess(path, token, responseType);
    }

    @Override
    public List<Cancellation> cancellations(String token, String uuid, String periodId) {

        log.info("Buscando cancelamentos pelo period id {}", periodId);

        String path = pathBase().concat("/").concat(uuid).concat("/cancellations?").concat("periodId=" + periodId);

        ParameterizedTypeReference<List<Cancellation>> responseType = new ParameterizedTypeReference<>() {
        };

        return requestProcess(path, token, responseType);
    }

    @Override
    public List<Sale> sales(String token, String uuid, LocalDate startDate, LocalDate endDate) {

        log.info("Buscando vendas realizadas no período de {} até {}", startDate, endDate);

        String path = pathBase().concat("/").concat(uuid).concat("/sales?").concat("beginOrderDate=" + startDate)
                .concat("&endOrderDate=" + endDate);

        ParameterizedTypeReference<List<Sale>> responseType = new ParameterizedTypeReference<>() {
        };

        return requestProcess(path, token, responseType);
    }

    @Override
    public List<SaleAdjustment> salesAdjustments(final String token, final String uuid, final LocalDate startDate, final LocalDate endDate) {

        log.info("Buscando ajuste de vendas no periodo de {} até {}", startDate, endDate);

        String path = pathBase().concat("/").concat(uuid).concat("/salesAdjustments?").concat("beginUpdateDate=" + startDate)
                .concat("&endUpdateDate=" + endDate);

        ParameterizedTypeReference<List<SaleAdjustment>> responseType = new ParameterizedTypeReference<>() {
        };

        return requestProcess(path, token, responseType);
    }

    @Override
    public List<Payment> payments(String token, String uuid, LocalDate startDate, LocalDate endDate) {

        log.info("Buscando pagamentos no periodo de {} até {}", startDate, endDate);

        String path = pathBase().concat("/").concat(uuid).concat("/payments?").concat("beginExpectedExecutionDate=" + startDate)
                .concat("&endExpectedExecutionDate=" + endDate);

        ParameterizedTypeReference<List<Payment>> responseType = new ParameterizedTypeReference<>() {
        };

        return requestProcess(path, token, responseType);
    }

    @Override
    public String pathBase() {
        return "https://merchant-api.ifood.com.br/financial/v2.0/merchants";
    }
}

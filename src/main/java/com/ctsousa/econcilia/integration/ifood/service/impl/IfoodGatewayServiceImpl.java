package com.ctsousa.econcilia.integration.ifood.service.impl;

import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.integration.ifood.IfoodGateway;
import com.ctsousa.econcilia.integration.ifood.entity.*;
import com.ctsousa.econcilia.integration.ifood.service.FinancialService;
import com.ctsousa.econcilia.integration.ifood.service.MerchantService;
import com.ctsousa.econcilia.integration.ifood.service.TokenService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class IfoodGatewayServiceImpl implements IfoodGateway {

    private final TokenService tokenService;

    private final MerchantService merchantService;

    private final FinancialService financialService;

    private Token token;

    public IfoodGatewayServiceImpl(TokenService tokenService, MerchantService merchantService, FinancialService financialService) {
        this.tokenService = tokenService;
        this.merchantService = merchantService;
        this.financialService = financialService;
        this.gerarToken();
    }

    @Override
    public List<MaintenanceFee> findMaintenanceFees(String uuid, LocalDate startDate, LocalDate endDate) {
        if (isTokenNaoValido()) {
            gerarToken();
        }

        return financialService.maintenanceFees(token.getAccessToken(), uuid, startDate, endDate);
    }

    @Override
    public List<Occurrence> findOccurences(String uuid, LocalDate startDate, LocalDate endDate) {
        if (isTokenNaoValido()) {
            gerarToken();
        }

        return financialService.occurrences(token.getAccessToken(), uuid, startDate, endDate);
    }

    @Override
    public List<ReceivableRecord> findReceivables(String uuid, LocalDate startDate, LocalDate endDate) {
        if (isTokenNaoValido()) {
            gerarToken();
        }

        return financialService.receivableRecords(token.getAccessToken(), uuid, startDate, endDate);
    }

    @Override
    public List<IncomeTaxe> findIncomeTaxes(String uuid, LocalDate startDate, LocalDate endDate) {
        if (isTokenNaoValido()) {
            gerarToken();
        }

        return financialService.incomeTaxes(token.getAccessToken(), uuid, startDate, endDate);
    }

    @Override
    public void verifyMerchantById(String uuid) {
        if (isTokenNaoValido()) {
            gerarToken();
        }

        Merchant merchant = merchantService.details(uuid, token.getAccessToken());

        if (merchant == null) {
            throw new NotificacaoException(String.format("Não existe nenhum merchant cadastrado com loja id %s na base do ifood.", uuid));
        }
    }

    @Override
    public List<Sale> findSalesBy(String uuid, LocalDate startDate, LocalDate endDate) {
        if (isTokenNaoValido()) {
            gerarToken();
        }

        return financialService.sales(token.getAccessToken(), uuid, startDate, endDate);
    }

    @Override
    public List<SaleAdjustment> findSaleAdjustmentBy(String uuid, LocalDate startDate, LocalDate endDate) {
        if (isTokenNaoValido()) {
            gerarToken();
        }

        return financialService.salesAdjustments(token.getAccessToken(), uuid, startDate, endDate);
    }

    @Override
    public List<Payment> findPaymentBy(String uuid, LocalDate startDate, LocalDate endDate) {
        if (isTokenNaoValido()) {
            gerarToken();
        }

        return financialService.payments(token.getAccessToken(), uuid, startDate, endDate);
    }

    @Override
    public List<Cancellation> findCancellationBy(String uuid, LocalDate startDate, LocalDate endDate) {

        if (isTokenNaoValido()) {
            gerarToken();
        }

        return financialService.cancellations(token.getAccessToken(), uuid, startDate, endDate);
    }

    @Override
    public List<ChargeCancellation> findChargeCancellationBy(String uuid, LocalDate startDate, LocalDate endDate) {
        if (isTokenNaoValido()) {
            gerarToken();
        }

        return financialService.chargeCancellations(token.getAccessToken(), uuid, startDate, endDate);
    }

    private boolean isTokenNaoValido() {
        return !tokenService.isValido();
    }

    private void gerarToken() {
        token = tokenService.gerarToken();
    }
}

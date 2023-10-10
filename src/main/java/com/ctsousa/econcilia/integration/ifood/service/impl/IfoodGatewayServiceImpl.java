package com.ctsousa.econcilia.integration.ifood.service.impl;

import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.integration.ifood.IfoodGateway;
import com.ctsousa.econcilia.integration.ifood.entity.Merchant;
import com.ctsousa.econcilia.integration.ifood.entity.Sale;
import com.ctsousa.econcilia.integration.ifood.entity.SaleAdjustment;
import com.ctsousa.econcilia.integration.ifood.entity.Token;
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

    private boolean isTokenNaoValido() {
        return !tokenService.isValido();
    }

    private void gerarToken() {
        token = tokenService.gerarToken();
    }
}

package com.ctsousa.econcilia.integration.ifood.service.impl;

import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.integration.ifood.IfoodGateway;
import com.ctsousa.econcilia.integration.ifood.entity.Merchant;
import com.ctsousa.econcilia.integration.ifood.entity.Sale;
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

    private String token;

    public IfoodGatewayServiceImpl(TokenService tokenService, MerchantService merchantService, FinancialService financialService) {
        this.tokenService = tokenService;
        this.merchantService = merchantService;
        this.financialService = financialService;
        this.gerarToken();
    }

    @Override
    public void verifyMerchantById(String uuid) {
        Merchant merchant = merchantService.details(uuid, token);

        if (merchant == null) {
            throw new NotificacaoException(String.format("Não existe nenhum merchant cadastrado com loja id %s na base do ifood.", uuid));
        }
    }

    @Override
    public List<Sale> findSalesBy(String uuid, LocalDate startDate, LocalDate endDate) {
        return financialService.sales(token, uuid, startDate, endDate);
    }

    private void gerarToken() {
        if (token == null) {
            token = tokenService.getAccessToken();
        }
    }
}

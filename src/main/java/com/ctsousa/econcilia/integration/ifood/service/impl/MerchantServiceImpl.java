package com.ctsousa.econcilia.integration.ifood.service.impl;

import com.ctsousa.econcilia.integration.ifood.AbstractIfoodService;
import com.ctsousa.econcilia.integration.ifood.entity.Merchant;
import com.ctsousa.econcilia.integration.ifood.service.MerchantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MerchantServiceImpl extends AbstractIfoodService implements MerchantService {

    @Override
    public Merchant details(String uuid, final String token) {
        try {
            log.info("Buscando detalhes do merchat com loja id..::: {}", uuid);

            String path = pathBase().concat("/").concat(uuid);

            var response = restTemplate.exchange(
                path,
                HttpMethod.GET,
                new HttpEntity<>(getHttpHeaders(token)),
                Merchant.class
            );

            return response.getBody();
        }
        catch (Exception e) {
            return null;
        }
    }

    @Override
    public String pathBase() {
        return "https://merchant-api.ifood.com.br/merchant/v1.0/merchants";
    }
}
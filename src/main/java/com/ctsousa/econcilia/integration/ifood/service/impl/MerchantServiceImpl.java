package com.ctsousa.econcilia.integration.ifood.service.impl;

import com.ctsousa.econcilia.integration.ifood.AbstractIfoodService;
import com.ctsousa.econcilia.integration.ifood.entity.Cancellation;
import com.ctsousa.econcilia.integration.ifood.entity.Merchant;
import com.ctsousa.econcilia.integration.ifood.service.MerchantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class MerchantServiceImpl extends AbstractIfoodService implements MerchantService {

    @Override
    public Merchant details(String uuid, final String token) {
        try {
            log.info("Buscando detalhes do merchat com loja id..::: {}", uuid);

            String path = pathBase().concat("/").concat(uuid);

            return requestProcess(path, token, Merchant.class);
        }
        catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Merchant> all(String token) {
        log.info("Listando todos merchants permitidos pelo ifood.");
        ParameterizedTypeReference<List<Merchant>> responseType = new ParameterizedTypeReference<>() { };

        return requestProcess(pathBase(), token, responseType);
    }

    @Override
    public String pathBase() {
        return "https://merchant-api.ifood.com.br/merchant/v1.0/merchants";
    }
}

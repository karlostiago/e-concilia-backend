package com.ctsousa.econcilia.integration.ifood.service;

import com.ctsousa.econcilia.integration.ifood.entity.Merchant;
import org.springframework.stereotype.Service;

@Service
public interface MerchantService {

    Merchant details(final String uuid, final String token);
}

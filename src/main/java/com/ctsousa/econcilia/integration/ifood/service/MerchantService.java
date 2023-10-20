package com.ctsousa.econcilia.integration.ifood.service;

import com.ctsousa.econcilia.integration.ifood.entity.Merchant;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MerchantService {

    Merchant details(final String uuid, final String token);

    List<Merchant> all(final String token);
}

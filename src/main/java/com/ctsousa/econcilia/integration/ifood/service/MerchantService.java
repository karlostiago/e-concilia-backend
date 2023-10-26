package com.ctsousa.econcilia.integration.ifood.service;

import com.ctsousa.econcilia.integration.ifood.entity.Merchant;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MerchantService {

    List<Merchant> all(final String token);
}

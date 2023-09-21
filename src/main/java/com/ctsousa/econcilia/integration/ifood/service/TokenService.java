package com.ctsousa.econcilia.integration.ifood.service;

import com.ctsousa.econcilia.integration.ifood.entity.Token;
import org.springframework.stereotype.Service;

@Service
public interface TokenService {

    Token gerarToken();

    boolean isValido();
}

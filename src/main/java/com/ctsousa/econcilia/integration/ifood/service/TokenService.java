package com.ctsousa.econcilia.integration.ifood.service;

import org.springframework.stereotype.Service;

@Service
public interface TokenService {

    String getAccessToken();
}

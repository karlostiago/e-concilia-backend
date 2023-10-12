package com.ctsousa.econcilia.integration.ifood.service.impl;

import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.integration.ifood.AbstractIfoodService;
import com.ctsousa.econcilia.integration.ifood.entity.Token;
import com.ctsousa.econcilia.integration.ifood.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Calendar;
import java.util.Date;

@Slf4j
@Component
public class TokenServiceImpl extends AbstractIfoodService implements TokenService {

    private static final long EXPIRA_EM_3H = 10800L;

    private static final long MIL_MILISSEGUNDOS = 1000L;

    @Value("${ifood.credencial.client-id}")
    private String clientId;

    @Value("${ifood.credencial.client-secret}")
    private String clientSecret;

    private final WebClient webClient;

    private Calendar calendar = Calendar.getInstance();

    public TokenServiceImpl(WebClient webClient) {
        this.webClient = webClient;
        calcularTempoDeExpirar();
    }

    @Override
    public Token gerarToken() {

        log.info("Gerando token de acesso a API do ifood.");

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(pathBase())
                .queryParam(GRANT_TYPE, "client_credentials")
                .queryParam(CLIENT_ID, clientId)
                .queryParam(CLIENT_SECRET, clientSecret);

        String url = uriBuilder.toUriString();

        Token token = webClient.post()
                .uri(url)
                .contentType(MediaType.valueOf("application/x-www-form-urlencoded"))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Token.class)
                .block();

        if (token == null) {
            throw new NotificacaoException("Não foi possível gerar access token.");
        }

        return token;
    }

    @Override
    public boolean isValido() {
        Date dataExpiraToken = calendar.getTime();
        Date dataSistema = new Date();

        if (dataSistema.getTime() > dataExpiraToken.getTime()) {
            calendar = Calendar.getInstance();
            calcularTempoDeExpirar();
            return false;
        }

        return true;
    }

    @Override
    public String pathBase() {
        return "https://merchant-api.ifood.com.br/authentication/v1.0/oauth/token";
    }

    private void calcularTempoDeExpirar() {
        calendar.setTimeInMillis(calendar.getTimeInMillis() + (EXPIRA_EM_3H * MIL_MILISSEGUNDOS));
    }
}

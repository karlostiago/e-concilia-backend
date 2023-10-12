package com.ctsousa.econcilia.integration.ifood;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public abstract class AbstractIfoodService {

    protected final RestTemplate restTemplate = new RestTemplate();

    protected static final String GRANT_TYPE = "grantType";

    protected static final String CLIENT_ID = "clientId";

    protected static final String CLIENT_SECRET = "clientSecret";

    public abstract String pathBase();

    public HttpHeaders getHttpHeaders(final String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        return headers;
    }

    protected <T> List<T> requestProcess(final String path, final String token, ParameterizedTypeReference<List<T>> responseType) {
        var response = restTemplate.exchange(
                path,
                HttpMethod.GET,
                new HttpEntity<>(getHttpHeaders(token)),
                responseType
        );

        return response.getBody();
    }

    protected <T> T requestProcess(final String path, final String token, Class<T> responseType) {
        var response = restTemplate.exchange(
                path,
                HttpMethod.GET,
                new HttpEntity<>(getHttpHeaders(token)),
                responseType
        );

        return response.getBody();
    }
}

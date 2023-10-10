package com.ctsousa.econcilia.integration.ifood;

import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

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
}

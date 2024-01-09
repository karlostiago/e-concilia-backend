package com.ctsousa.econcilia.integration.receitaws;

import com.ctsousa.econcilia.integration.receitaws.json.DadosCnpjJson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ReceitaWS {

    private final String URL = "https://receitaws.com.br/v1/cnpj/";

    private final RestTemplate restTemplate;

    public ReceitaWS(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public DadosCnpjJson consultarDadosCNPJ (final String cnpj) {
        ResponseEntity<DadosCnpjJson> response = this.restTemplate.getForEntity(URL + cnpj, DadosCnpjJson.class);

        if (!response.getStatusCode().equals(HttpStatus.OK)) {
            throw new RuntimeException();
        }

        return response.getBody();
    }
}

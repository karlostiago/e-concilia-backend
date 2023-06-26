package com.ctsousa.econcilia.integration.ifood.controller;

import com.ctsousa.econcilia.integration.ifood.model.Comerciante;
import com.ctsousa.econcilia.integration.ifood.model.DetalhesPedido;
import com.ctsousa.econcilia.integration.ifood.model.Pedido;
import com.ctsousa.econcilia.integration.ifood.model.TaxasManutencao;
import com.ctsousa.econcilia.integration.ifood.model.token.TokenAcesso;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/ifood")
public class IfoodWS {
    private static final String GRANT_TYPE = "grantType";
    private static final String CLIEN_ID = "clientId";
    private static final String CLIEN_SECRET = "clientSecret";
    private static final String AUTHORIZATION_CODE = "authorizationCode";
    private static final String AUTHORIZATION_CODE_VERIFIER = "authorizationCodeVerifier";
    private static final String REFRESH_TOKEN = "refreshToken";

    private static final String URL_ORDENS = "https://merchant-api.ifood.com.br/order/v1.0/events:polling";
    private static final String URL_COMERCIANTE = "https://merchant-api.ifood.com.br/merchant/v1.0/merchants";
    private static final String URL_FINANCEIRO = "https://merchant-api.ifood.com.br/financial/v2.0/merchants/";
    private static final String URL_ORDENS_DETAILHES = "https://merchant-api.ifood.com.br/order/v1.0/orders/";
    private static final String URL_AUTENTICACAO = "https://merchant-api.ifood.com.br/authentication/v1.0/oauth/token";

    @Value("${ifood.clientid}")
    private String clienId;

    @Value("${ifood.clientsecret}")
    private String clienSecret;
    private final WebClient webClient;

    @Autowired
    public IfoodWS(WebClient webClient) {
        this.webClient = webClient;
    }

    @GetMapping("ordens")
    public List<Pedido> getPedidos(@RequestBody(required = false) String id,@RequestBody(required = false) String secret) throws JsonProcessingException {
        verificaIdSecret(id,secret);
        return ifoodApi(URL_ORDENS, List.class);
    }

    @GetMapping("/comerciantes")
    public List<Comerciante> getComerciantes(@RequestBody(required = false) String id,@RequestBody(required = false) String secret) {
        verificaIdSecret(id,secret);
        return ifoodApi(URL_COMERCIANTE, List.class);
    }

    @GetMapping("/taxas_manutencao")
    public List<TaxasManutencao> getTaxasManutencao(@RequestBody(required = false) String id,@RequestBody(required = false) String secret, String idComerciante, String periodo) {
        verificaIdSecret(id,secret);
        return ifoodApiComParametros(URL_FINANCEIRO + "{idComerciante}/maintenanceFees", idComerciante, periodo, List.class);
    }

    @GetMapping("/detalhes_pedido")
    public DetalhesPedido getDetalhesPedido(@RequestBody(required = false) String id,@RequestBody(required = false) String secret, String ordemId) {
        verificaIdSecret(id,secret);
        return ifoodApi(URL_ORDENS_DETAILHES + ordemId, DetalhesPedido.class);
    }

    private <T> T ifoodApi(String url, Class<T> responseType) {
        String token = getTokenAcesso().getTokenAcesso();

        return webClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(responseType)
                .block();
    }

    private <T> T ifoodApiComParametros(String url, String idComerciante, String periodo, Class<T> responseType) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("periodId", periodo);

        String finalUrl = uriBuilder.buildAndExpand(idComerciante).toUriString();

        return ifoodApi(finalUrl, responseType);
    }

    public TokenAcesso getTokenAcesso() {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(URL_AUTENTICACAO)
                .queryParam(GRANT_TYPE, "client_credentials")
                .queryParam(CLIEN_ID, clienId)
                .queryParam(CLIEN_SECRET, clienSecret)
                .queryParam(AUTHORIZATION_CODE, "")
                .queryParam(AUTHORIZATION_CODE_VERIFIER, "")
                .queryParam(REFRESH_TOKEN, "");

        String url = uriBuilder.toUriString();

        TokenAcesso tokenAcesso = webClient.post()
                .uri(url)
                .contentType(MediaType.valueOf("application/x-www-form-urlencoded"))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(TokenAcesso.class)
                .block();

        return tokenAcesso;
    }
    private void verificaIdSecret(String id, String secret) {
        if (id != null) {
            clienId = id;
        }
        if (secret != null) {
            clienSecret = secret;
        }
    }
}

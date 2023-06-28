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

    private String GRANT_TYPE = "grantType";
    private String CLIENT_ID = "clientId";
    private String CLIENT_SECRET = "clientSecret";
    private String AUTHORIZATION_CODE = "authorizationCode";
    private String AUTHORIZATION_CODE_VERIFIER = "authorizationCodeVerifier";
    private String REFRESH_TOKEN = "refreshToken";

    @Value("${ifood.client-id}")
    private String clientId;

    @Value("${ifood.client-secret}")
    private String clientSecret;

    @Value("${urls.url-ordens}")
    private String urlOrdens;

    @Value("${urls.url-comerciantes}")
    private String urlComerciantes;

    @Value("${urls.url-financeiro}")
    private String urlFinanceiro;

    @Value("${urls.url-ordens-detalhes}")
    private String urlOrdensDetalhes;

    @Value("${urls.url-autenticacao}")
    private String urlAutenticacao;

    private final WebClient webClient;

    @Autowired
    public IfoodWS(WebClient webClient) {
        this.webClient = webClient;
    }

    @GetMapping("ordens")
    public List<Pedido> getPedidos(@RequestBody(required = false) String id,@RequestBody(required = false) String secret) throws JsonProcessingException {
        verificaIdSecret(id,secret);
        return ifoodApi(urlOrdens, List.class);
    }

    @GetMapping("/comerciantes")
    public List<Comerciante> getComerciantes(@RequestBody(required = false) String id,@RequestBody(required = false) String secret) {
        verificaIdSecret(id,secret);
        return ifoodApi(urlComerciantes, List.class);
    }

    @GetMapping("/taxas_manutencao")
    public List<TaxasManutencao> getTaxasManutencao(@RequestBody(required = false) String id,@RequestBody(required = false) String secret, String idComerciante, String periodo) {
        verificaIdSecret(id,secret);
        return ifoodApiComParametros(urlFinanceiro+ "{idComerciante}/maintenanceFees", idComerciante, periodo, List.class);
    }

    @GetMapping("/detalhes_pedido")
    public DetalhesPedido getDetalhesPedido(@RequestBody(required = false) String id,@RequestBody(required = false) String secret, String ordemId) {
        verificaIdSecret(id,secret);
        return ifoodApi(urlOrdensDetalhes + ordemId, DetalhesPedido.class);
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
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(urlAutenticacao)
                .queryParam(GRANT_TYPE, "client_credentials")
                .queryParam(CLIENT_ID, clientId)
                .queryParam(CLIENT_SECRET, clientSecret)
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
            clientId = id;
        }
        if (secret != null) {
            clientSecret = secret;
        }
    }
}

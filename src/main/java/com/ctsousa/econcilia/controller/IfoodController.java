package com.ctsousa.econcilia.controller;

import com.ctsousa.econcilia.model.ifood.Comerciante;
import com.ctsousa.econcilia.model.ifood.DetalhesPedido;
import com.ctsousa.econcilia.model.ifood.Pedido;
import com.ctsousa.econcilia.model.ifood.TaxasManutencao;
import com.ctsousa.econcilia.model.ifood.token.TokenAcesso;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/ifood")
public class IfoodController {

    private static final String GRANT_TYPE = "grantType";
    private static final String CLIEN_ID = "clientId";
    private static final String CLIEN_SECRET = "clientSecret";
    private static final String AUTHORIZATION_CODE = "authorizationCode";
    private static final String AUTHORIZATION_CODE_VERIFIER = "authorizationCodeVerifier";
    private static final String REFRESH_TOKEN = "refreshToken";

    private final WebClient webClient;

    @Autowired
    public IfoodController(WebClient webClient) {
        this.webClient = webClient;
    }

    @GetMapping("ordens")
    public List<Pedido> getPedidos() throws JsonProcessingException {
        String urlIfoodOrdens = "https://merchant-api.ifood.com.br/order/v1.0/events:polling";

        String token = getTokenAcesso().getTokenAcesso();


        List<Pedido> ordens = webClient.get()
                .uri(urlIfoodOrdens)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(List.class).block();


        if (ordens == null) {
            return null;
        }
        return ordens;
    }

    @GetMapping("/comerciantes")
    public List<Comerciante> getComerciantes() {
        String urlComerciante = "https://merchant-api.ifood.com.br/merchant/v1.0/merchants";

        String token = getTokenAcesso().getTokenAcesso();

        List<Comerciante> comerciante = webClient.get()
                .uri(urlComerciante)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(List.class).block();

        if (comerciante == null) {
            return null;
        }

        return comerciante;

    }

    @GetMapping("/taxas_manutencao")
    public List<TaxasManutencao> getTaxasManutencao(String idComerciante, String periodo) {
        String urlTaxasManutencao = "https://merchant-api.ifood.com.br/financial/v2.0/merchants/";
        String endpoint = urlTaxasManutencao + idComerciante + "/maintenanceFees";

        String token = getTokenAcesso().getTokenAcesso();

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(endpoint)
                .queryParam("periodId", periodo);

        String url = uriBuilder.toUriString();

        List<TaxasManutencao> taxasManutencao = webClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(List.class).block();

        if (taxasManutencao == null) {
            return null;
        }

        return taxasManutencao;

    }

    @GetMapping("/detalhes_pedido")
    public DetalhesPedido getDetalhesPedido(String ordemId) {
        String urlIdPedido = "https://merchant-api.ifood.com.br/order/v1.0/orders/";

        String token = getTokenAcesso().getTokenAcesso();

        DetalhesPedido detalhesPedido = webClient.get()
                .uri(urlIdPedido + ordemId)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(DetalhesPedido.class).block();

        if (detalhesPedido == null) {
            return null;
        }

        return detalhesPedido;

    }

    public TokenAcesso getTokenAcesso() {
        String urlAutenticationIfood = "https://merchant-api.ifood.com.br/authentication/v1.0/oauth/token";

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(urlAutenticationIfood)
                .queryParam(GRANT_TYPE, "client_credentials")
                .queryParam(CLIEN_ID, "9312b324-4b36-451f-b0ce-7671b8641751")
                .queryParam(CLIEN_SECRET, "llrirsr9pyc9rcugny4amqkol3m6s68qtmihaeif0puf7z7t64uy9ajy8l52n0klf0ijzu0ksn7ohg9r586xyzius7f11pze1g0")
                .queryParam(AUTHORIZATION_CODE, "")
                .queryParam(AUTHORIZATION_CODE_VERIFIER, "")
                .queryParam(REFRESH_TOKEN, "");

        String url = uriBuilder.toUriString();

        TokenAcesso tokenAcesso = webClient.post()
                .uri(url)
                .contentType(MediaType.valueOf("application/x-www-form-urlencoded"))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(TokenAcesso.class).block();

        return tokenAcesso;

    }

}

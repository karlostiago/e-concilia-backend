package com.ctsousa.econcilia.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class IfoodController {

    private final WebClient webClient;

    @Autowired
    public IfoodController(WebClient webClient) {
        this.webClient = webClient;
    }

    @GetMapping("ordens")
    public Mono<String> getFromExternalAPI() throws JsonProcessingException {
        String externalApiUrl = "https://merchant-api.ifood.com.br";


        return webClient.post()
                .uri(externalApiUrl + "/authentication/v1.0/oauth/token?grantType=client_credentials&clientId=9312b324-4b36-451f-b0ce-7671b8641751&clientSecret=llrirsr9pyc9rcugny4amqkol3m6s68qtmihaeif0puf7z7t64uy9ajy8l52n0klf0ijzu0ksn7ohg9r586xyzius7f11pze1g0&authorizationCode=&authorizationCodeVerifier=&refreshToken=")
                .contentType(MediaType.valueOf("application/x-www-form-urlencoded"))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class);



    //    String ordens = String.valueOf(webClient.get()
      //          .uri(externalApiUrl+"/order/v1.0/events:polling?types=PLC%2CREC%2CCFM&groups=ORDER_STATUS%2CDELIVERY")
        //        .accept(MediaType.APPLICATION_JSON)
         //       .header(HttpHeaders.AUTHORIZATION, "Bearer")
         //       .retrieve()
         //       .bodyToMono(String.class));


    }

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello, World!";
    }
}

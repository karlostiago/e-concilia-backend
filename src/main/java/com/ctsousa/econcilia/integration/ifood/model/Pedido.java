package com.ctsousa.econcilia.integration.ifood.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Pedido {

    @JsonProperty("id")
    private String id;

    @JsonProperty("code")
    private String codigo;

    @JsonProperty("fullCode")
    private String codigoCompleto;

    @JsonProperty("orderId")
    private String ordemId;

    @JsonProperty("merchantId")
    private String comercianteId;

    @JsonProperty("createdAt")
    private ZonedDateTime criadoEm;

}
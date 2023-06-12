package com.ctsousa.econcilia.integration.ifood.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MetodoPagamento {

    @JsonProperty("wallet")
    private Carteira carteira;

    @JsonProperty("method")
    private String metodo;

    @JsonProperty("prepaid")
    private boolean prePago;

    @JsonProperty("currency")
    private String moeda;

    @JsonProperty("type")
    private String tipo;

    @JsonProperty("value")
    private int valor;

    @JsonProperty("cash")
    private Dinheiro dinheiro;

    @JsonProperty("card")
    private Cartao cartao;
}

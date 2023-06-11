package com.ctsousa.econcilia.integration.ifood.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Escala {

    @JsonProperty("minQuantity")
    private int quantidadeMinima;

    @JsonProperty("price")
    private int preco;
}

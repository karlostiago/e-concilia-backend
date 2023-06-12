package com.ctsousa.econcilia.integration.ifood.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValorPatrocinio {

    @JsonProperty("name")
    private String nome;

    @JsonProperty("value")
    private int valor;

    @JsonProperty("description")
    private String descricao;
}

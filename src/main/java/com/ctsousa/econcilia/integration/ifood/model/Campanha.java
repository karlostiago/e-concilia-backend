package com.ctsousa.econcilia.integration.ifood.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Campanha {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String nome;
}

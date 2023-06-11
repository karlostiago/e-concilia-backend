package com.ctsousa.econcilia.integration.ifood.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Beneficio {

    @JsonProperty("targetId")
    private String idAlvo;

    @JsonProperty("sponsorshipValues")
    private List<ValorPatrocinio> valoresPatrocinio;

    @JsonProperty("value")
    private int valor;

    @JsonProperty("target")
    private String alvo;

    @JsonProperty("campaign")
    private Campanha campanha;
}

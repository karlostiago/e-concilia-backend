package com.ctsousa.econcilia.model.ifood;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TaxaAdicional {

    @JsonProperty("type")
    private String tipo;

    @JsonProperty("value")
    private int valor;

    @JsonProperty("description")
    private String descricao;

    @JsonProperty("fullDescription")
    private String descricaoCompleta;

    @JsonProperty("liabilities")
    private List<Responsabilidade> responsabilidades;
}

package com.ctsousa.econcilia.integration.ifood.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Coleta {

    @JsonProperty("picker")
    private String coletador;

    @JsonProperty("replacementOptions")
    private String opcoesSubstituicao;

}

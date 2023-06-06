package com.ctsousa.econcilia.model.ifood;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Telefone {

    @JsonProperty("number")
    private String numero;

    @JsonProperty("localizer")
    private String localizador;

    @JsonProperty("localizerExpiration")
    private String expiracaoLocalizador;
}

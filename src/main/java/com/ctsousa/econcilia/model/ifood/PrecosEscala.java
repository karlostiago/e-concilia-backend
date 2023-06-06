package com.ctsousa.econcilia.model.ifood;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PrecosEscala {

    @JsonProperty("defaultPrice")
    private int precoPadrao;

    @JsonProperty("scales")
    private List<Escala> escalas;
}

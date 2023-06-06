package com.ctsousa.econcilia.model.ifood;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Coordenadas {

    @JsonProperty("latitude")
    private double latitude;

    @JsonProperty("longitude")
    private double longitude;
}

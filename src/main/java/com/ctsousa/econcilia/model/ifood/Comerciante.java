package com.ctsousa.econcilia.model.ifood;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Comerciante {

    @JsonProperty("name")
    private String nome;

    @JsonProperty("id")
    private String id;
}

package com.ctsousa.econcilia.model.ifood;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Dinheiro {

    @JsonProperty("changeFor")
    private int trocoPara;
}

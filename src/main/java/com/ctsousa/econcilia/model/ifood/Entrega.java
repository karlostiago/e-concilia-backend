package com.ctsousa.econcilia.model.ifood;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Entrega {

    @JsonProperty("mode")
    private String modo;

    @JsonProperty("deliveredBy")
    private String entreguePor;

    @JsonProperty("deliveryAddress")
    private EnderecoEntrega enderecoEntrega;

    @JsonProperty("deliveryDateTime")
    private String dataHoraEntrega;
}

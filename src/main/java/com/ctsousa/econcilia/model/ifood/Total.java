package com.ctsousa.econcilia.model.ifood;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Total {

    @JsonProperty("benefits")
    private int beneficios;

    @JsonProperty("deliveryFee")
    private int taxaEntrega;

    @JsonProperty("orderAmount")
    private int valorPedido;

    @JsonProperty("subTotal")
    private int subTotal;

    @JsonProperty("additionalFees")
    private int taxasAdicionais;
}

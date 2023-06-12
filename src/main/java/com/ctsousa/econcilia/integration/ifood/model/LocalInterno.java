package com.ctsousa.econcilia.integration.ifood.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocalInterno {

    @JsonProperty("mode")
    private String modo;

    @JsonProperty("deliveryDateTime")
    private String dataHoraEntrega;

    @JsonProperty("table")
    private String mesa;
}

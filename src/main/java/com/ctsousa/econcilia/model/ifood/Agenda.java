package com.ctsousa.econcilia.model.ifood;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Agenda {

    @JsonProperty("deliveryDateTimeStart")
    private String dataHoraInicioEntrega;

    @JsonProperty("deliveryDateTimeEnd")
    private String dataHoraFimEntrega;
}

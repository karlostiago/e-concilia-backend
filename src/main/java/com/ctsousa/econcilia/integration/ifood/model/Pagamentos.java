package com.ctsousa.econcilia.integration.ifood.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Pagamentos {

    @JsonProperty("methods")
    private List<MetodoPagamento> metodos;

    @JsonProperty("pending")
    private int pendente;

    @JsonProperty("prepaid")
    private int prePago;
}

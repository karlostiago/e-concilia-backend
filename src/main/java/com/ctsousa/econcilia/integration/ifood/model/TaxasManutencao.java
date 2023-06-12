package com.ctsousa.econcilia.integration.ifood.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TaxasManutencao {

    @JsonProperty("periodoId")
    private String periodoId;

    @JsonProperty("dataPagamentoPrevista")
    private LocalDate dataPagamentoPrevista;

    @JsonProperty("idTransacao")
    private int idTransacao;

    @JsonProperty("dataTransacao")
    private LocalDate dataTransacao;

    @JsonProperty("tipo")
    private String tipo;

    @JsonProperty("valor")
    private double valor;
}

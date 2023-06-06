package com.ctsousa.econcilia.model.ifood;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnderecoEntrega {

    @JsonProperty("reference")
    private String referencia;

    @JsonProperty("country")
    private String pais;

    @JsonProperty("streetName")
    private String nomeRua;

    @JsonProperty("formattedAddress")
    private String enderecoFormatado;

    @JsonProperty("streetNumber")
    private String numeroRua;

    @JsonProperty("city")
    private String cidade;

    @JsonProperty("postalCode")
    private String codigoPostal;

    @JsonProperty("coordinates")
    private Coordenadas coordenadas;

    @JsonProperty("neighborhood")
    private String bairro;

    @JsonProperty("state")
    private String estado;

    @JsonProperty("complement")
    private String complemento;
}

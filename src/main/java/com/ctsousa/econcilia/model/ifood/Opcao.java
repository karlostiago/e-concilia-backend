package com.ctsousa.econcilia.model.ifood;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Opcao {

    @JsonProperty("unitPrice")
    private int precoUnitario;

    @JsonProperty("unit")
    private String unidade;

    @JsonProperty("ean")
    private String ean;

    @JsonProperty("quantity")
    private int quantidade;

    @JsonProperty("externalCode")
    private String codigoExterno;

    @JsonProperty("price")
    private int preco;

    @JsonProperty("name")
    private String nome;

    @JsonProperty("index")
    private int indice;

    @JsonProperty("id")
    private String id;

    @JsonProperty("addition")
    private int adicional;
}

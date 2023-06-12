package com.ctsousa.econcilia.integration.ifood.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Item {

    @JsonProperty("unitPrice")
    private int precoUnitario;

    @JsonProperty("quantity")
    private int quantidade;

    @JsonProperty("externalCode")
    private String codigoExterno;

    @JsonProperty("totalPrice")
    private int precoTotal;

    @JsonProperty("index")
    private int indice;

    @JsonProperty("unit")
    private String unidade;

    @JsonProperty("ean")
    private String ean;

    @JsonProperty("price")
    private int preco;

    @JsonProperty("scalePrices")
    private PrecosEscala precosEscala;

    @JsonProperty("observations")
    private String observacoes;

    @JsonProperty("imageUrl")
    private String urlImagem;

    @JsonProperty("name")
    private String nome;

    @JsonProperty("options")
    private List<Opcao> opcoes;

    @JsonProperty("id")
    private String id;

    @JsonProperty("optionsPrice")
    private int precoOpcoes;
}

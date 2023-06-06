package com.ctsousa.econcilia.model.ifood;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Cliente {

    @JsonProperty("phone")
    private Telefone telefone;

    @JsonProperty("documentNumber")
    private String numeroDocumento;

    @JsonProperty("name")
    private String nome;

    @JsonProperty("ordersCountOnMerchant")
    private int quantidadePedidosComerciante;

    @JsonProperty("segmentation")
    private String segmentacao;

    @JsonProperty("id")
    private String id;
}

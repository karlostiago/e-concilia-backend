package com.ctsousa.econcilia.model.ifood;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.boot.Metadata;

import java.time.ZonedDateTime;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Pedido {

    @JsonProperty("id")
    private String id;

    @JsonProperty("code")
    private String codigo;

    @JsonProperty("fullCode")
    private String codigoCompleto;

    @JsonProperty("orderId")
    private String ordemId;

    @JsonProperty("merchantId")
    private String comercianteId;

    @JsonProperty("createdAt")
    private ZonedDateTime criadoEm;

}
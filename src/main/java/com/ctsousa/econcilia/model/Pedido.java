package com.ctsousa.econcilia.model;

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
    private String code;

    @JsonProperty("fullCode")
    private String fullCode;

    @JsonProperty("orderId")
    private String orderId;

    @JsonProperty("merchantId")
    private String merchantId;

    @JsonProperty("createdAt")
    private ZonedDateTime createdAt;

}
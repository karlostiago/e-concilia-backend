package com.ctsousa.econcilia.model.dto;

import org.hibernate.boot.Metadata;

import java.time.ZonedDateTime;

public class PedidoDTO {

    private String id;
    private String code;
    private String fullCode;
    private String orderId;
    private Metadata metadata;
    private ZonedDateTime createdAt;

}

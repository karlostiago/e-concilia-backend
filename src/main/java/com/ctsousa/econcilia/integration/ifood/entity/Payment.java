package com.ctsousa.econcilia.integration.ifood.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Payment {

    private String type;

    private String method;

    private String brand;

    private String liability;

    private String cardNumber;

    private String nsu;
}

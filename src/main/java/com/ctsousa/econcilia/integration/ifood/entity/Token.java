package com.ctsousa.econcilia.integration.ifood.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Token {

    private String accessToken;

    private long expiresIn;
}

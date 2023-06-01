package com.ctsousa.econcilia.model;

public class AcessToken {

    private String accessToken;
    private String type;
    private int expiresIn;

    public AcessToken(String accessToken, String type, int expiresIn) {
        this.accessToken = accessToken;
        this.type = type;
        this.expiresIn = expiresIn;
    }
}

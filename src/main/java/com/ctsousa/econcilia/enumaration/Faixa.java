package com.ctsousa.econcilia.enumaration;

import lombok.Getter;

@Getter
public enum Faixa {

    FX_30(30),

    FX_60(60),

    FX_90(90),

    FX_120(120),

    FX_180(180);

    private final int valor;

    Faixa(int valor) {
        this.valor = valor;
    }
}

package com.ctsousa.econcilia.enumaration;

import lombok.Getter;

@Getter
public enum Faixa {

    FX_30(30, 1),

    FX_60(60, 2),

    FX_90(90, 3),

    FX_120(120, 4),

    FX_180(180, 5);

    private final int dias;

    private final int meses;

    Faixa(int dias, int meses) {
        this.dias = dias;
        this.meses = meses;
    }
}

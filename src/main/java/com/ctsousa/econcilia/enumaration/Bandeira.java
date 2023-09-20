package com.ctsousa.econcilia.enumaration;

import lombok.Getter;

@Getter
public enum Bandeira {

    MASTERCARD("Mastercard"),

    MASTERCARD_MASTRO("Mastercard maestro"),

    VISA("Visa"),

    VISA_ELECTRON("Visa electron"),

    IFOOD_MEAL_VOUCHER("Ifood voucher"),

    MOVILE_PAY("Movile pay"),

    ELO("Elo"),

    HIPERCARD("Hipercard");

    private final String descricao;

    Bandeira(final String descricao) {
        this.descricao = descricao;
    }

    public static Bandeira porDescricao(final String descricao) {
        for (Bandeira produto : Bandeira.values()) {
            if (produto.name().equalsIgnoreCase(descricao)) {
                return produto;
            }
        }
        return null;
    }
}

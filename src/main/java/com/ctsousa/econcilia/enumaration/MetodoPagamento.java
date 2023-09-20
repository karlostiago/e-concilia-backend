package com.ctsousa.econcilia.enumaration;

import lombok.Getter;

@Getter
public enum MetodoPagamento {

    PIX("Pix"),

    DEBIT("Débito"),

    CREDIT("Crédito"),

    MEAL_VOUCHER("Voucher"),

    CASH("Dinheiro"),

    BANK_PAY("Bank pay");

    private final String descricao;

    MetodoPagamento(final String descricao) {
        this.descricao = descricao;
    }

    public static MetodoPagamento porDescricao(final String descricao) {
        for (MetodoPagamento metodoPagamento : MetodoPagamento.values()) {
            if (metodoPagamento.name().equalsIgnoreCase(descricao)) {
                return metodoPagamento;
            }
        }
        return null;
    }
}

package com.ctsousa.econcilia.enumaration;

import lombok.Getter;

@Getter
public enum TipoNotificacao {

    GLOBAL(1),

    DIRETA(2);

    private final Integer codigo;

    TipoNotificacao(Integer codigo) {
        this.codigo = codigo;
    }

    public static TipoNotificacao porCodigo(final Integer codigo) {
        for (TipoNotificacao tipo : TipoNotificacao.values()) {
            if (tipo.getCodigo().equals(codigo)) {
                return tipo;
            }
        }
        return null;
    }
}

package com.ctsousa.econcilia.enumaration;

public enum TipoValor {
    MONETARIO,
    PERCENTUAL;

    public static TipoValor porDescricao(final String descricao) {
        for (TipoValor tipo : TipoValor.values()) {
            if (tipo.name().equalsIgnoreCase(descricao)) {
                return tipo;
            }
        }
        return null;
    }
}

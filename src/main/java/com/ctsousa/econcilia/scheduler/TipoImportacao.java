package com.ctsousa.econcilia.scheduler;

public enum TipoImportacao {

    IFOOD("IFOOD");

    private final String descricao;

    TipoImportacao(String descricao) {
        this.descricao = descricao;
    }

    public static TipoImportacao porDescricao(final String processador) {
        for (TipoImportacao tipoProcessador : TipoImportacao.values()) {
            if (tipoProcessador.name().equalsIgnoreCase(processador)) {
                return tipoProcessador;
            }
        }
        return null;
    }
}

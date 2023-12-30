package com.ctsousa.econcilia.enumaration;

import lombok.Getter;

@Getter
public enum ImportacaoSituacao {

    AGENDADA("Agendada"),

    EM_PROCESSAMENTO("Em processamento"),

    PROCESSADO("Processado");

    private final String descricao;

    ImportacaoSituacao(final String descricao) {
        this.descricao = descricao;
    }

    public static ImportacaoSituacao porDescricao(final String descricao) {
        for (ImportacaoSituacao situacao : ImportacaoSituacao.values()) {
            if (situacao.name().equalsIgnoreCase(descricao)) {
                return situacao;
            }
        }
        return null;
    }
}

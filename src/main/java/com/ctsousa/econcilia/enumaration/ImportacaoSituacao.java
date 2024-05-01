package com.ctsousa.econcilia.enumaration;

import lombok.Getter;

@Getter
public enum ImportacaoSituacao {

    AGENDADO("Agendado"),

    EM_PROCESSAMENTO("Em processamento"),

    PROCESSADO("Processado"),

    ERRO_PROCESSAMENTO("Erro Processamento");

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

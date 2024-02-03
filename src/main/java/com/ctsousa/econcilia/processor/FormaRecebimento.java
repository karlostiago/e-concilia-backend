package com.ctsousa.econcilia.processor;

import com.ctsousa.econcilia.util.StringUtil;
import lombok.Getter;

@Getter
public enum FormaRecebimento {

    LOJA("Loja"),

    OPERADORA("Operadora");

    private final String descricao;

    FormaRecebimento(String descricao) {
        this.descricao = descricao;
    }

    public static FormaRecebimento porDescricao(final String descricao) {
        for (FormaRecebimento formaRecebimento : FormaRecebimento.values()) {
            if (StringUtil.temValor(descricao) && formaRecebimento.getDescricao().equalsIgnoreCase(descricao)) {
                return formaRecebimento;
            }
        }
        return null;
    }
}

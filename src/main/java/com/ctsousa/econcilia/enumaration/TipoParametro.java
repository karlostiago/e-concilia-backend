package com.ctsousa.econcilia.enumaration;

import lombok.Getter;

import static com.ctsousa.econcilia.util.StringUtil.temValor;

@Getter
public enum TipoParametro {
    IMPORTACAO_PROGRAMADA("Importação programada", "Ativa ou desativa o serviço de importação programada."),
    IMPORTACAO_DIARIA("Importação diária", "Ativa ou desativa o serviço de importação diária"),
    CONSOLIDACAO("Consolidação de vendas", "Ativa ou desativa o serviço de consolidação de vendas"),
    INTEGRACAO_AUTOMATICA("Integração automática", "Ativa ou desativa o serviço de integração automática de novas empresas e operadoras."),
    AJUSTE_VENDA("Ajuste venda", "Ativa ou desativa serviço para atualização automática registro de vendas, ajuste de vendas, ocorrências e cancelamentos.");

    private final String preFixo;
    private final String descricao;

    TipoParametro(String preFixo, String descricao) {
        this.preFixo = preFixo;
        this.descricao = descricao;
    }

    public static TipoParametro por(final String tipoParametro) {
        for (TipoParametro parametro : TipoParametro.values()) {
            if (temValor(tipoParametro) && parametro.name().equalsIgnoreCase(tipoParametro)) {
                return parametro;
            }
        }
        return null;
    }
}

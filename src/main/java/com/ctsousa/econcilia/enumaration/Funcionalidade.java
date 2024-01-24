package com.ctsousa.econcilia.enumaration;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Funcionalidade {

    ROLE_PESQUISAR_DASHBOARD(1, TipoFuncionalidade.PESQUISAR),

    ROLE_CADASTRAR_EMPRESA(2, TipoFuncionalidade.CADASTRAR),
    ROLE_PESQUISAR_EMPRESA(2, TipoFuncionalidade.PESQUISAR),
    ROLE_EDITAR_EMPRESA(2, TipoFuncionalidade.ATUALIZAR),
    ROLE_DELETAR_EMPRESA(2, TipoFuncionalidade.DELETAR),
    ROLE_ATIVAR_EMPRESA(2, TipoFuncionalidade.ATIVAR),

    ROLE_CADASTRAR_CONTRATO(3, TipoFuncionalidade.CADASTRAR),
    ROLE_PESQUISAR_CONTRATO(3, TipoFuncionalidade.PESQUISAR),
    ROLE_EDITAR_CONTRATO(3, TipoFuncionalidade.ATUALIZAR),
    ROLE_DELETAR_CONTRATO(3, TipoFuncionalidade.DELETAR),
    ROLE_ATIVAR_CONTRATO(3, TipoFuncionalidade.ATIVAR),

    ROLE_CADASTRAR_OPERADORA(4, TipoFuncionalidade.CADASTRAR),
    ROLE_PESQUISAR_OPERADORA(4, TipoFuncionalidade.PESQUISAR),
    ROLE_EDITAR_OPERADORA(4, TipoFuncionalidade.ATUALIZAR),
    ROLE_DELETAR_OPERADORA(4, TipoFuncionalidade.DELETAR),

    ROLE_PESQUISAR_TAXA(5, TipoFuncionalidade.PESQUISAR),
    ROLE_ATIVAR_TAXA(5, TipoFuncionalidade.ATIVAR),

    ROLE_PESQUISAR_CONCILIADOR_IFOOD(6, TipoFuncionalidade.PESQUISAR),

    ROLE_CADASTRAR_INTEGRACAO(7, TipoFuncionalidade.CADASTRAR),
    ROLE_PESQUISAR_INTEGRACAO(7, TipoFuncionalidade.PESQUISAR),
    ROLE_EDITAR_INTEGRACAO(7, TipoFuncionalidade.ATUALIZAR),
    ROLE_DELETAR_INTEGRACAO(7, TipoFuncionalidade.DELETAR),

    ROLE_CADASTRAR_USUARIO(8, TipoFuncionalidade.CADASTRAR),
    ROLE_PESQUISAR_USUARIO(8, TipoFuncionalidade.PESQUISAR),
    ROLE_EDITAR_USUARIO(8, TipoFuncionalidade.ATUALIZAR),
    ROLE_DELETAR_USUARIO(8, TipoFuncionalidade.DELETAR),

    ROLE_CADASTRAR_PERMISSAO(9, TipoFuncionalidade.CADASTRAR),
    ROLE_PESQUISAR_PERMISSAO(9, TipoFuncionalidade.PESQUISAR),
    ROLE_EDITAR_PERMISSAO(9, TipoFuncionalidade.ATUALIZAR),
    ROLE_DELETAR_PERMISSAO(9, TipoFuncionalidade.DELETAR),

    ROLE_AGENDAR_IMPORTACAO(10, TipoFuncionalidade.AGENDAR),
    ROLE_PESQUISAR_IMPORTACAO(10, TipoFuncionalidade.PESQUISAR);

    private final Integer codigo;

    private final TipoFuncionalidade tipoFuncionalidade;

    Funcionalidade(Integer codigo, TipoFuncionalidade tipoFuncionalidade) {
        this.codigo = codigo;
        this.tipoFuncionalidade = tipoFuncionalidade;
    }

    public static Funcionalidade por(final Integer codigo, TipoFuncionalidade tipoFuncionalidade) {
        return Arrays.stream(Funcionalidade.values())
                .filter(funcionalidade -> funcionalidade.codigo.equals(codigo) && funcionalidade.tipoFuncionalidade.equals(tipoFuncionalidade))
                .findFirst()
                .orElse(null);
    }

    public static String [] todas() {
        return Arrays.stream(Funcionalidade.values())
                .map(Funcionalidade::name)
                .toArray(String[]::new);
    }
}

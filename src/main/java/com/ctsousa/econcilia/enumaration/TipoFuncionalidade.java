package com.ctsousa.econcilia.enumaration;

import com.ctsousa.econcilia.exceptions.NotificacaoException;

public enum TipoFuncionalidade {

    PESQUISAR,

    ATUALIZAR,

    CADASTRAR,

    ATIVAR,

    DELETAR,

    MENU,

    AGENDAR;

    public static TipoFuncionalidade por(final String tipo) {
        String [] tipos = tipo.split("_");

        if (tipos.length == 0) return null;

        return switch (tipos[0].toUpperCase()) {
            case "PESQUISAR" -> PESQUISAR;
            case "ATUALIZAR" -> ATUALIZAR;
            case "CADASTRAR" -> CADASTRAR;
            case "ATIVAR" -> ATIVAR;
            case "DELETAR" -> DELETAR;
            case "AGENDAR" -> AGENDAR;
            case "MENU" -> MENU;
            default ->
                throw new NotificacaoException("Nenhum tipo funcionalidade corresponde ao tipo funcionalidade selecionado ::: " + tipo);
        };
    }
}

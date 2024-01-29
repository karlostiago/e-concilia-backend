package com.ctsousa.econcilia.security;

public abstract class Autorizar {

    /**
     * Regras de permissao para tela inicial
     */
    public static final String PESQUISAR_DASHBOARD = "hasAuthority('ROLE_PESQUISAR_DASHBOARD')";

    /**
     * Regras de permissao para tela de empresas
     */
    public static final String CADASTRAR_EMPRESA = "hasAuthority('ROLE_CADASTRAR_EMPRESA')";
    public static final String PESQUISAR_EMPRESA = "hasAuthority('ROLE_PESQUISAR_EMPRESA')";
    public static final String EDITAR_EMPRESA = "hasAuthority('ROLE_EDITAR_EMPRESA')";
    public static final String DELETAR_EMPRESA = "hasAuthority('ROLE_DELETAR_EMPRESA')";
    public static final String ATIVAR_EMPRESA = "hasAuthority('ROLE_ATIVAR_EMPRESA')";

    /**
     * Regras de permissao para tela de contratos
     */
    public static final String CADASTRAR_CONTRATO = "hasAuthority('ROLE_CADASTRAR_CONTRATO')";
    public static final String PESQUISAR_CONTRATO = "hasAuthority('ROLE_PESQUISAR_CONTRATO')";
    public static final String EDITAR_CONTRATO = "hasAuthority('ROLE_EDITAR_CONTRATO')";
    public static final String DELETAR_CONTRATO = "hasAuthority('ROLE_DELETAR_CONTRATO')";
    public static final String ATIVAR_CONTRATO = "hasAuthority('ROLE_ATIVAR_CONTRATO')";

    /**
     * Regras de permissao para tela de operadoras
     */
    public static final String CADASTRAR_OPERADORA = "hasAuthority('ROLE_CADASTRAR_OPERADORA')";
    public static final String PESQUISAR_OPERADORA = "hasAuthority('ROLE_PESQUISAR_OPERADORA')";
    public static final String EDITAR_OPERADORA = "hasAuthority('ROLE_EDITAR_OPERADORA')";
    public static final String DELETAR_OPERADORA = "hasAuthority('ROLE_DELETAR_OPERADORA')";

    /**
     * Regras de permissao para tela de taxas
     */
    public static final String PESQUISAR_TAXA = "hasAuthority('ROLE_PESQUISAR_TAXA')";
    public static final String ATIVAR_TAXA = "hasAuthority('ROLE_ATIVAR_TAXA')";

    /**
     * Regras de permissao para tela de conciliação ifood
     */
    public static final String PESQUISAR_CONCILIADOR_IFOOD = "hasAuthority('ROLE_PESQUISAR_CONCILIADOR_IFOOD')";

    /**
     * Regras de permissao para tela de integração
     */
    public static final String CADASTRAR_INTEGRACAO = "hasAuthority('ROLE_CADASTRAR_INTEGRACAO')";
    public static final String PESQUISAR_INTEGRACAO = "hasAuthority('ROLE_PESQUISAR_INTEGRACAO')";
    public static final String EDITAR_INTEGRACAO = "hasAuthority('ROLE_EDITAR_INTEGRACAO')";
    public static final String DELETAR_INTEGRACAO = "hasAuthority('ROLE_DELETAR_INTEGRACAO')";

    /**
     * Regras de permissao para tela de cadastro de usuario
     */
    public static final String CADASTRAR_USUARIO = "hasAuthority('ROLE_CADASTRAR_USUARIO')";
    public static final String PESQUISAR_USUARIO = "hasAuthority('ROLE_PESQUISAR_USUARIO')";
    public static final String EDITAR_USUARIO = "hasAuthority('ROLE_EDITAR_USUARIO')";
    public static final String DELETAR_USUARIO = "hasAuthority('ROLE_DELETAR_USUARIO')";


    /**
     * Regras de permissao para tela de permissao de usuario
     */
    public static final String CADASTRAR_PERMISSAO = "hasAuthority('ROLE_CADASTRAR_PERMISSAO')";
    public static final String PESQUISAR_PERMISSAO = "hasAuthority('ROLE_PESQUISAR_PERMISSAO')";
    public static final String EDITAR_PERMISSAO = "hasAuthority('ROLE_EDITAR_PERMISSAO')";
    public static final String DELETAR_PERMISSAO = "hasAuthority('ROLE_DELETAR_PERMISSAO')";

    /**
     * Regras de permissao para tela de importação
     */
    public static final String AGENDAR_IMPORTACAO = "hasAuthority('ROLE_AGENDAR_IMPORTACAO')";
    public static final String PESQUISAR_IMPORTACAO = "hasAuthority('ROLE_PESQUISAR_IMPORTACAO')";

    private Autorizar() { }
}

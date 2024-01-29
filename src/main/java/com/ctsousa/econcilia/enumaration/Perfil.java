package com.ctsousa.econcilia.enumaration;

import com.ctsousa.econcilia.model.Permissao;
import com.ctsousa.econcilia.model.Usuario;
import com.ctsousa.econcilia.service.PermissaoService;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public enum Perfil {

    ADMINISTRADOR("Administrador") {
        @Override
        public void aplicar(Usuario usuario, PermissaoService permissaoService) {
            List<Funcionalidade> funcionalidades = Arrays.stream(Funcionalidade.values())
                    .filter(f -> !f.getTipoFuncionalidade().equals(TipoFuncionalidade.DELETAR))
                    .toList();

            salvar(usuario, funcionalidades, permissaoService);
        }
    },

    MASTER("Master") {
        @Override
        public void aplicar(Usuario usuario, PermissaoService permissaoService) {
            List<Funcionalidade> funcionalidades = Arrays.stream(Funcionalidade.values())
                    .toList();

            salvar(usuario, funcionalidades, permissaoService);
        }
    },

    CONCILIADOR("Conciliador") {
        @Override
        public void aplicar(Usuario usuario, PermissaoService permissaoService) {
            List<Funcionalidade> funcionalidades = new ArrayList<>();
            funcionalidades.add(Funcionalidade.ROLE_PESQUISAR_DASHBOARD);
            funcionalidades.add(Funcionalidade.ROLE_PESQUISAR_EMPRESA);
            funcionalidades.add(Funcionalidade.ROLE_PESQUISAR_OPERADORA);
            funcionalidades.add(Funcionalidade.ROLE_PESQUISAR_INTEGRACAO);
            funcionalidades.add(Funcionalidade.ROLE_PESQUISAR_CONCILIADOR_IFOOD);
            funcionalidades.add(Funcionalidade.ROLE_MENU_CONCILIADOR);

            salvar(usuario, funcionalidades, permissaoService);
        }
    };

    private final String descricao;

    Perfil(String descricao) {
        this.descricao = descricao;
    }

    public abstract void aplicar(Usuario usuario, PermissaoService permissaoService);

    private static void salvar(Usuario usuario, List<Funcionalidade> funcionalidades, PermissaoService permissaoService) {
        Permissao permissao = permissaoService.pesquisar(usuario);

        if (permissao == null) {
            permissao = new Permissao();
            permissao.setUsuario(usuario);
            permissao.setFuncionalidades(funcionalidades);
            permissaoService.salvar(permissao);
        } else {
            permissao.setFuncionalidades(funcionalidades);
            permissaoService.atualizar(permissao.getId(), permissao);
        }

    }

    public static Perfil porDescricao(final String descricao) {
        for (Perfil perfil : Perfil.values()) {
            if (perfil.getDescricao().equalsIgnoreCase(descricao)) {
                return perfil;
            }
        }
        return null;
    }
}

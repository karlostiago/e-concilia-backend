package com.ctsousa.econcilia.model;

import com.ctsousa.econcilia.util.StringUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static com.ctsousa.econcilia.util.StringUtil.somenteNumero;

@Data
@Entity
@Table(name = "empresa")
@EqualsAndHashCode(callSuper = false)
public class Empresa extends Entidade {

    @NotNull
    @Column(name = "razao_social", nullable = false, length = 100)
    private String razaoSocial;

    @Column(name = "nome_fantasia", nullable = false, length = 100)
    private String nomeFantasia;

    @Column(nullable = false, length = 14)
    private String cnpj;

    @Embedded
    private Endereco endereco;

    @Embedded
    private Contato contato;

    @Column(columnDefinition = "default false")
    private boolean ativo;

    @PreUpdate
    @PrePersist
    public void pre () {
        setCnpj(somenteNumero(getCnpj()));
        getEndereco().setCep(somenteNumero(getEndereco().getCep()));
        getContato().setTelefone(somenteNumero(getContato().getTelefone()));
        getContato().setCelular(somenteNumero(getContato().getCelular()));
    }
}




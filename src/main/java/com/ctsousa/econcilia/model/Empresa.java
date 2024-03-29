package com.ctsousa.econcilia.model;

import com.ctsousa.econcilia.annotation.ExcludedCoverage;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static com.ctsousa.econcilia.util.StringUtil.maiuscula;
import static com.ctsousa.econcilia.util.StringUtil.somenteNumero;

@Getter
@Setter
@Entity
@Table(name = "empresa")
@EqualsAndHashCode(callSuper = false)
@ExcludedCoverage
public class Empresa extends Entidade {

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

    @Column(columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean ativo;

    public Empresa() {
    }

    public Empresa(final Long id) {
        setId(id);
    }

    @PreUpdate
    @PrePersist
    public void init() {
        setCnpj(somenteNumero(getCnpj()));
        setRazaoSocial(maiuscula(getRazaoSocial()));
        setNomeFantasia(maiuscula(getNomeFantasia()));

        getEndereco().setCep(somenteNumero(getEndereco().getCep()));
        getContato().setTelefone(somenteNumero(getContato().getTelefone()));
        getContato().setCelular(somenteNumero(getContato().getCelular()));
    }
}



package com.ctsousa.econcilia.model;

import com.ctsousa.econcilia.annotation.ExcludedCoverage;
import com.ctsousa.econcilia.enumaration.Perfil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static com.ctsousa.econcilia.util.StringUtil.maiuscula;

@Getter
@Setter
@Entity
@Table(name = "usuario")
@EqualsAndHashCode(callSuper = false)
@ExcludedCoverage
public class Usuario extends Entidade {

    @NotNull
    @Column(name = "nome_completo", nullable = false)
    private String nomeCompleto;

    @NotNull
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotNull
    @Column(name = "senha", nullable = false)
    private String senha;

    @NotNull
    @Column(name = "lojas_permitidas", nullable = false)
    private String lojasPermitidas;

    @Enumerated(EnumType.STRING)
    @Column(name = "perfil")
    private Perfil perfil;

    @PrePersist
    @PreUpdate
    public void init() {
        this.nomeCompleto = maiuscula(this.nomeCompleto);
        this.email = maiuscula(this.email);
    }

    public Usuario() {
    }

    public Usuario(String nomeCompleto) {
        setNomeCompleto(nomeCompleto);
    }
}

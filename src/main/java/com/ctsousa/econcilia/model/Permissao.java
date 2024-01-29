package com.ctsousa.econcilia.model;

import com.ctsousa.econcilia.enumaration.Funcionalidade;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "permissao")
@EqualsAndHashCode(callSuper = false)
public class Permissao extends Entidade {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ElementCollection(targetClass = Funcionalidade.class)
    @CollectionTable(name = "permissao_funcionalidade", joinColumns = @JoinColumn(name = "permissao_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "funcionalidade", nullable = false)
    private List<Funcionalidade> funcionalidades;
}

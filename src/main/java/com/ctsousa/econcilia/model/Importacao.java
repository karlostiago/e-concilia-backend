package com.ctsousa.econcilia.model;

import com.ctsousa.econcilia.enumaration.ImportacaoSituacao;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "importacao")
@EqualsAndHashCode(callSuper = false)
public class Importacao extends Entidade {

    @Column(name = "data_inicial", nullable = false)
    private LocalDate dataInicial;

    @Column(name = "data_final", nullable = false)
    private LocalDate dataFinal;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @ManyToOne
    @JoinColumn(name = "operadora_id")
    private Operadora operadora;

    @Enumerated(EnumType.STRING)
    @Column(name = "situacao", nullable = false)
    private ImportacaoSituacao situacao;
}

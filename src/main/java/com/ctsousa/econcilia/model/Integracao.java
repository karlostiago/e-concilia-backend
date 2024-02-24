package com.ctsousa.econcilia.model;

import com.ctsousa.econcilia.annotation.ExcludedCoverage;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "integracao", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"operadora_id", "codigo_integracao"})
})
@EqualsAndHashCode(callSuper = false)
@ExcludedCoverage
public class Integracao extends Entidade {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operadora_id", nullable = false)
    private Operadora operadora;

    @Column(name = "codigo_integracao", nullable = false)
    private String codigoIntegracao;
}

package com.ctsousa.econcilia.model;

import com.ctsousa.econcilia.annotation.ExcludedCoverage;
import com.ctsousa.econcilia.enumaration.TipoParametro;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "parametro")
@EqualsAndHashCode(callSuper = false)
@ExcludedCoverage
public class Parametro extends Entidade {

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_parametro", nullable = false)
    private TipoParametro tipoParametro;

    @Column(columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean ativo;
}

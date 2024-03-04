package com.ctsousa.econcilia.model;

import com.ctsousa.econcilia.annotation.ExcludedCoverage;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "cancelamento")
@EqualsAndHashCode(callSuper = false)
@ExcludedCoverage
public class Cancelamento extends Entidade {

    @Column(name = "nome_comerciante", nullable = false)
    private String nomeComerciante;

    @Column(name = "comerciante_id", nullable = false)
    private String comercianteId;

    @Column(name = "pedido_id", nullable = false)
    private String pedidoId;

    @Column(name = "periodo_id", nullable = false)
    private String periodoId;

    @Column(name = "valor", columnDefinition = "decimal(19,2) default 0.0")
    private BigDecimal valor;
}

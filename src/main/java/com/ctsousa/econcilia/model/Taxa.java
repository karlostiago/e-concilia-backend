package com.ctsousa.econcilia.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static com.ctsousa.econcilia.util.StringUtil.maiuscula;
import static com.ctsousa.econcilia.util.StringUtil.somenteNumero;

@Getter
@Setter
@Entity
@Table(name = "taxa")
@EqualsAndHashCode(callSuper = false)
public class Taxa extends Entidade {

    @NotNull
    @Column(name = "descricao", nullable = false, length = 100)
    private String descricao;

    @NotNull
    @Column(name = "valor", nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(name = "entra_em_vigor", nullable = false)
    private LocalDate entraEmVigor;

    @Column(name = "valido_ate", nullable = false)
    private LocalDate validoAte;

    @ManyToOne
    @JoinColumn(name = "contrato_id")
    private Contrato contrato;

    @Column(name = "ativo", nullable = false, columnDefinition = "boolean default false ")
    private Boolean ativo;

    @Transient
    public Long expiraEm() {
        return ChronoUnit.DAYS.between(entraEmVigor, validoAte);
    }

    @PreUpdate
    @PrePersist
    public void init() {
        setDescricao(maiuscula(getDescricao()));
    }
}

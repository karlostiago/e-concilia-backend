package com.ctsousa.econcilia.model;

import com.ctsousa.econcilia.annotation.ExcludedCoverage;
import com.ctsousa.econcilia.enumaration.TipoNotificacao;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "notificacao")
@EqualsAndHashCode(callSuper = false)
@ExcludedCoverage
public class Notificacao extends Entidade {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @NotNull
    @Column(name = "mensagem", nullable = false)
    private String mensagem;

    @Column(name = "resolvida", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean resolvida = Boolean.FALSE;

    @Column(name = "lida", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean lida = Boolean.FALSE;

    @NotNull
    @Column(name = "tipo", nullable = false)
    private Integer tipoNotificacao;

    public void setTipoNotificacao(Integer tipoNotificacao) {
        this.tipoNotificacao = -1;

        TipoNotificacao notificacao = TipoNotificacao.porCodigo(tipoNotificacao);

        if (notificacao != null) {
            this.tipoNotificacao = notificacao.getCodigo();
        }
    }
}

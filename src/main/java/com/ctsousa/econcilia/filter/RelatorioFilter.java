package com.ctsousa.econcilia.filter;

import com.ctsousa.econcilia.enumaration.TipoRelatorio;
import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Operadora;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class RelatorioFilter {

    private LocalDate dataInicial;
    private LocalDate dataFinal;
    private Empresa empresa;
    private Operadora operadora;
    private TipoRelatorio tipoRelatorio;

    public RelatorioFilter(LocalDate dataInicial, LocalDate dataFinal, Long empresaId, Long operadoraId, TipoRelatorio tipoRelatorio) {
        setDataInicial(dataInicial);
        setDataFinal(dataFinal);
        setEmpresa(empresaId);
        setOperadora(operadoraId);
        setTipoRelatorio(tipoRelatorio);
    }

    public void setDataInicial(LocalDate dataInicial) {
        if (dataInicial == null) {
            throw new NotificacaoException("Campo data inicial não informada.");
        }
        this.dataInicial = dataInicial;
    }

    public void setDataFinal(LocalDate dataFinal) {
        if (dataFinal == null) {
            throw new NotificacaoException("Campo data final não informada.");
        }
        this.dataFinal = dataFinal;
    }

    public void setEmpresa(Long empresaId) {
        if (empresaId == null) {
            throw new NotificacaoException("Campo empresa não informada.");
        }
        this.empresa = new Empresa(empresaId);
    }

    public void setOperadora(Long operadoraId) {
        if (operadoraId == null) {
            throw new NotificacaoException("Campo operadora não informada.");
        }
        this.operadora = new Operadora(operadoraId);
    }

    public void setTipoRelatorio(TipoRelatorio tipoRelatorio) {
        if (tipoRelatorio == null) {
            throw new NotificacaoException("Campo tipo relatório não informada.");
        }
        this.tipoRelatorio = tipoRelatorio;
    }
}

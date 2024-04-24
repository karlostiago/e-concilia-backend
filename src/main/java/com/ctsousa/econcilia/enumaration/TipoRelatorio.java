package com.ctsousa.econcilia.enumaration;

import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.filter.RelatorioFilter;
import com.ctsousa.econcilia.service.GeradorRelatorioCSVService;

public enum TipoRelatorio {

    CSV {
        @Override
        public byte[] gerar(GeradorRelatorioCSVService service, RelatorioFilter relatorioFilter) {
            return service.gerarCSV(relatorioFilter.getDataInicial(), relatorioFilter.getDataFinal(), relatorioFilter.getEmpresa(), relatorioFilter.getOperadora());
        }
    },

    PDF {
        @Override
        public byte[] gerar(GeradorRelatorioCSVService service, RelatorioFilter relatorioFilter) {
            throw new UnsupportedOperationException("Operação não suportada.");
        }
    };

    public abstract byte [] gerar(GeradorRelatorioCSVService service, RelatorioFilter relatorioFilter);

    public static TipoRelatorio porDescricao(final String descricao) {
        for (TipoRelatorio tipo : TipoRelatorio.values()) {
            if (tipo.name().equalsIgnoreCase(descricao)) {
                return tipo;
            }
        }
        return null;
    }
}

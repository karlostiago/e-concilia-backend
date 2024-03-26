package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.model.*;

import java.time.LocalDate;
import java.util.List;

public interface IntegracaoIfoodService {

    List<Venda> pesquisarVendas(final String codigoIntegracao, final LocalDate dtInicial, final LocalDate dtFinal);

    List<AjusteVenda> pesquisarAjusteVendas(final String codigoIntegracao, final LocalDate dtInicial, final LocalDate dtFinal);

    List<Pagamento> pesquisarPagamentos(final String codigoIntegracao, final LocalDate dtInicial, final LocalDate dtFinal);

    List<Cancelamento> pesquisarCancelamentos(final String codigoIntegracao, final String periodoId);

    List<CobrancaCancelada> pesquisarCobrancaCanceladas(final String codigoIntegracao, final LocalDate dtInicial, final LocalDate dtFinal);

    List<TaxaManutencao> pesquisarTaxasManutencao(final String codigoIntegracao, final LocalDate dtInicial, final LocalDate dtFinal);

    List<ImpostoRenda> pesquisarImpostoRenda(final String codigoIntegracao, final LocalDate dtInicial, final LocalDate dtFinal);

    List<RegistroContaReceber> pesquisarRegistroContaReceber(final String codigoIntegracao, final LocalDate dtInicial, final LocalDate dtFinal);

    List<Ocorrencia> pesquisarOcorrencias(final String codigoIntegracao, final LocalDate dtInicial, final LocalDate dtFinal);
}

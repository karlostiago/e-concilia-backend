package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.model.AjusteVenda;
import com.ctsousa.econcilia.model.Integracao;
import com.ctsousa.econcilia.model.Venda;
import com.ctsousa.econcilia.model.dto.IntegracaoDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface IntegracaoService {

    Integracao salvar (final Integracao integracao);

    List<Integracao> pesquisar (final Long empresaId, final Long operadoraId, final String codigoIntegracao);

    void deletar (final Long id);

    Integracao atualizar (final Long id, final IntegracaoDTO integracaoDTO);

    Integracao pesquisarPorId (final Long id);

    Integracao pesquisarPorCodigoIntegracao(final String codigoIntegracao);

    List<Venda> pesquisarVendasIfood(final String codigoIntegracao, final String metodoPagamento, final String bandeira, LocalDate dtInicial, LocalDate dtFinal);

    List<AjusteVenda> pesquisarAjusteVendasIfood(final String codigoIntegracao, final LocalDate dtInicial, final LocalDate dtFinal);
}

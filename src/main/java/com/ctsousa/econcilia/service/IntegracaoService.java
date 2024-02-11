package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Integracao;
import com.ctsousa.econcilia.model.Operadora;
import com.ctsousa.econcilia.model.dto.IntegracaoDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IntegracaoService {

    Integracao salvar(final Integracao integracao);

    List<Integracao> pesquisar(final Long empresaId, final Long operadoraId, final String codigoIntegracao);

    void deletar(final Long id);

    Integracao atualizar(final Long id, final IntegracaoDTO integracaoDTO);

    Integracao pesquisarPorId(final Long id);

    Integracao pesquisarPorCodigoIntegracao(final String codigoIntegracao);

    Integracao pesquisar(final Empresa empresa, final Operadora operadora);
}

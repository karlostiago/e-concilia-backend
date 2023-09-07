package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.model.VinculaEmpresaOperadora;
import com.ctsousa.econcilia.model.dto.VinculaEmpresaOperadoraDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface VinculaEmpresaOperadoraService {

    VinculaEmpresaOperadora salvar (final VinculaEmpresaOperadora vinculaEmpresaOperadora);

    List<VinculaEmpresaOperadora> pesquisar (final Long empresaId, final Long operadoraId, final String codigoIntegracao);

    void deletar (final Long id);

    VinculaEmpresaOperadora atualizar (final Long id, final VinculaEmpresaOperadoraDTO vinculaEmpresaOperadoraDTO);

    VinculaEmpresaOperadora pesquisarPorId (final Long id);

    VinculaEmpresaOperadora pesquisarPorCodigoIntegracao(final String codigoIntegracao);
}

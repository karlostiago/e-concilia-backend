package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.assembler.impl.VinculaEmpresaOperadoraMapper;
import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Operadora;
import com.ctsousa.econcilia.model.VinculaEmpresaOperadora;
import com.ctsousa.econcilia.model.dto.VinculaEmpresaOperadoraDTO;
import com.ctsousa.econcilia.repository.VinculaEmpresaOperadoraRepository;
import com.ctsousa.econcilia.service.VinculaEmpresaOperadoraService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VinculaEmpresaOperadoraServiceImpl implements VinculaEmpresaOperadoraService {

    private final VinculaEmpresaOperadoraRepository vinculaEmpresaOperadoraRepository;

    private final VinculaEmpresaOperadoraMapper vinculaEmpresaOperadoraMapper;

    public VinculaEmpresaOperadoraServiceImpl(VinculaEmpresaOperadoraRepository vinculaEmpresaOperadoraRepository, VinculaEmpresaOperadoraMapper vinculaEmpresaOperadoraMapper) {
        this.vinculaEmpresaOperadoraRepository = vinculaEmpresaOperadoraRepository;
        this.vinculaEmpresaOperadoraMapper = vinculaEmpresaOperadoraMapper;
    }

    @Override
    public VinculaEmpresaOperadora salvar(VinculaEmpresaOperadora vinculaEmpresaOperadora) {

        if (vinculaEmpresaOperadoraRepository.existsByOperadoraAndCodigoIntegracao(vinculaEmpresaOperadora.getOperadora(), vinculaEmpresaOperadora.getCodigoIntegracao())) {
            throw new NotificacaoException("Já existe uma configuração vinculando código de integração..:: " + vinculaEmpresaOperadora.getCodigoIntegracao() + ", a operadora..:: " + vinculaEmpresaOperadora.getOperadora().getDescricao());
        }

        return vinculaEmpresaOperadoraRepository.save(vinculaEmpresaOperadora);
    }

    @Override
    public List<VinculaEmpresaOperadora> pesquisar(final Long empresaId, final Long operadoraId, final String codigoIntegracao) {

        List<VinculaEmpresaOperadora> vinculaEmpresaOperadoraList;

        var empresa = new Empresa(empresaId);
        var operadora = new Operadora(operadoraId);

        if (codigoIntegracao != null && !codigoIntegracao.isEmpty()) {
            return List.of(pesquisarPorCodigoIntegracao(codigoIntegracao));
        }

        if (empresaId != null && operadoraId != null) {
            vinculaEmpresaOperadoraList = vinculaEmpresaOperadoraRepository.findByEmpresaAndOperadora(empresa, operadora);
        }
        else if (empresaId != null) {
            vinculaEmpresaOperadoraList = vinculaEmpresaOperadoraRepository.findByEmpresa(empresa);
        }
        else if (operadoraId != null) {
            vinculaEmpresaOperadoraList = vinculaEmpresaOperadoraRepository.findByOperadora(operadora);
        }
        else {
            vinculaEmpresaOperadoraList = vinculaEmpresaOperadoraRepository.findAll();
        }

        return vinculaEmpresaOperadoraList;
    }

    @Override
    public void deletar(Long id) {
        var vinculaEmpresaOperadora = pesquisarPorId(id);
        vinculaEmpresaOperadoraRepository.delete(vinculaEmpresaOperadora);
    }

    @Override
    public VinculaEmpresaOperadora atualizar(Long id, VinculaEmpresaOperadoraDTO vinculaEmpresaOperadoraDTO) {
        pesquisarPorId(id);

        vinculaEmpresaOperadoraDTO.setId(id);
        VinculaEmpresaOperadora vinculaEmpresaOperadora = vinculaEmpresaOperadoraMapper.paraEntidade(vinculaEmpresaOperadoraDTO);
        vinculaEmpresaOperadoraRepository.save(vinculaEmpresaOperadora);

        return vinculaEmpresaOperadora;
    }

    @Override
    public VinculaEmpresaOperadora pesquisarPorId(Long id) {
        return vinculaEmpresaOperadoraRepository.porID(id)
                .orElseThrow(() -> new NotificacaoException(String.format("Configuração de vinculo empresa operadora com id %d não encontrado", id)));
    }

    @Override
    public VinculaEmpresaOperadora pesquisarPorCodigoIntegracao(String codigoIntegracao) {
        return vinculaEmpresaOperadoraRepository.findByCodigoIntegracao(codigoIntegracao)
                .orElseThrow(() -> new NotificacaoException(String.format("Configuração de vinculo empresa operadora com código de integração %s não encontrado", codigoIntegracao)));
    }
}

package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.integration.ifood.gateway.IfoodGateway;
import com.ctsousa.econcilia.mapper.impl.IntegracaoMapper;
import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Integracao;
import com.ctsousa.econcilia.model.Operadora;
import com.ctsousa.econcilia.model.dto.IntegracaoDTO;
import com.ctsousa.econcilia.repository.IntegracaoRepository;
import com.ctsousa.econcilia.service.IntegracaoService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IntegracaoServiceImpl implements IntegracaoService {

    private final IntegracaoRepository integracaoRepository;

    private final IntegracaoMapper integracaoMapper;

    private final IfoodGateway ifoodGateway;

    public IntegracaoServiceImpl(IntegracaoRepository integracaoRepository, IntegracaoMapper integracaoMapper, IfoodGateway ifoodGateway) {
        this.integracaoRepository = integracaoRepository;
        this.integracaoMapper = integracaoMapper;
        this.ifoodGateway = ifoodGateway;
    }

    @Override
    public Integracao salvar(Integracao integracao) {
        var codigoIntegracao = integracao.getCodigoIntegracao().trim();

        ifoodGateway.verifyMerchantById(codigoIntegracao);

        if (integracaoRepository.existsByOperadoraAndCodigoIntegracao(integracao.getOperadora(), codigoIntegracao)) {
            throw new NotificacaoException("Já existe uma integração..:: " + codigoIntegracao + ", com a operadora..:: " + integracao.getOperadora().getDescricao());
        }

        return integracaoRepository.save(integracao);
    }

    @Override
    public List<Integracao> pesquisar(final Long empresaId, final Long operadoraId, final String codigoIntegracao) {

        List<Integracao> integracoes;

        var empresa = new Empresa(empresaId);
        var operadora = new Operadora(operadoraId);

        if (codigoIntegracao != null && !codigoIntegracao.isEmpty()) {
            return List.of(pesquisarPorCodigoIntegracao(codigoIntegracao));
        }

        if (empresaId != null && operadoraId != null) {
            integracoes = integracaoRepository.findByEmpresaAndOperadora(empresa, operadora);
        } else if (empresaId != null) {
            integracoes = integracaoRepository.findByEmpresa(empresa);
        } else if (operadoraId != null) {
            integracoes = integracaoRepository.findByOperadora(operadora);
        } else {
            integracoes = integracaoRepository.findAll();
        }

        return integracoes;
    }

    @Override
    public Integracao pesquisar(Empresa empresa, Operadora operadora) {
        List<Integracao> integracoes = this.pesquisar(empresa.getId(), operadora.getId(), null);

        if (integracoes.isEmpty()) {
            throw new NotificacaoException("Não existe integração para a empresa com id ::: " + empresa.getId());
        }

        if (integracoes.size() > 1) {
            throw new NotificacaoException("Existe mais de uma integração para a empresa com id ::: " + empresa.getId());
        }

        return integracoes.get(0);
    }

    @Override
    public void deletar(Long id) {
        var integracao = pesquisarPorId(id);
        integracaoRepository.delete(integracao);
    }

    @Override
    public Integracao atualizar(Long id, IntegracaoDTO integracaoDTO) {
        pesquisarPorId(id);

        integracaoDTO.setId(id);
        Integracao integracao = integracaoMapper.paraEntidade(integracaoDTO);
        integracaoRepository.save(integracao);

        return integracao;
    }

    @Override
    public Integracao pesquisarPorId(Long id) {
        return integracaoRepository.porID(id)
                .orElseThrow(() -> new NotificacaoException(String.format("Integracao com id %d não encontrado", id)));
    }

    @Override
    public Integracao pesquisarPorCodigoIntegracao(String codigoIntegracao) {
        return integracaoRepository.findByCodigoIntegracao(codigoIntegracao)
                .orElseThrow(() -> new NotificacaoException(String.format("Integração com código integração %s não encontrado", codigoIntegracao)));
    }
}

package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.integration.ifood.IfoodGateway;
import com.ctsousa.econcilia.integration.ifood.entity.Sale;
import com.ctsousa.econcilia.mapper.IntegracaoMapper;
import com.ctsousa.econcilia.mapper.VendaMapper;
import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Integracao;
import com.ctsousa.econcilia.model.Operadora;
import com.ctsousa.econcilia.model.Venda;
import com.ctsousa.econcilia.model.dto.IntegracaoDTO;
import com.ctsousa.econcilia.repository.IntegracaoRepository;
import com.ctsousa.econcilia.service.IntegracaoService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Component
public class IntegracaoServiceImpl implements IntegracaoService {

    private final IntegracaoRepository integracaoRepository;

    private final IntegracaoMapper integracaoMapper;

    private final IfoodGateway ifoodGateway;

    private final VendaMapper mapper;

    public IntegracaoServiceImpl(IntegracaoRepository integracaoRepository, IntegracaoMapper integracaoMapper, IfoodGateway ifoodGateway, VendaMapper mapper) {
        this.integracaoRepository = integracaoRepository;
        this.integracaoMapper = integracaoMapper;
        this.ifoodGateway = ifoodGateway;
        this.mapper = mapper;
    }

    @Override
    public List<Venda> pesquisarVendasIfood(String codigoIntegracao, LocalDate dtInicial, LocalDate dtFinal) {

        long dias = ChronoUnit.DAYS.between(dtInicial, dtFinal);

        if (dias > 90) {
            throw new NotificacaoException("O período não pode ser maior que 90 dias");
        }

        List<Sale> sales = ifoodGateway.findSalesBy(codigoIntegracao, dtInicial, dtFinal);

        if (sales.isEmpty()) {
            return new ArrayList<>();
        }

        return mapper.paraLista(sales);
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
        }
        else if (empresaId != null) {
            integracoes = integracaoRepository.findByEmpresa(empresa);
        }
        else if (operadoraId != null) {
            integracoes = integracaoRepository.findByOperadora(operadora);
        }
        else {
            integracoes = integracaoRepository.findAll();
        }

        return integracoes;
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

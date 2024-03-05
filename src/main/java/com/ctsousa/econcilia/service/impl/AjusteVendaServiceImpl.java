package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.model.AjusteVenda;
import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Integracao;
import com.ctsousa.econcilia.model.Operadora;
import com.ctsousa.econcilia.repository.AjusteVendaRepository;
import com.ctsousa.econcilia.service.AjusteVendaService;
import com.ctsousa.econcilia.service.IntegracaoIfoodService;
import com.ctsousa.econcilia.service.IntegracaoService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class AjusteVendaServiceImpl implements AjusteVendaService {

    private final IntegracaoIfoodService integracaoIfoodService;

    private final AjusteVendaRepository ajusteVendaRepository;

    private final IntegracaoService integracaoService;

    public AjusteVendaServiceImpl(IntegracaoIfoodService integracaoIfoodService, AjusteVendaRepository ajusteVendaRepository, IntegracaoService integracaoService) {
        this.integracaoIfoodService = integracaoIfoodService;
        this.ajusteVendaRepository = ajusteVendaRepository;
        this.integracaoService = integracaoService;
    }

    @Override
    public List<AjusteVenda> buscar(final String codigoLoja, final LocalDate dataInicial, final LocalDate dataFinal) {
        Integracao integracao = integracaoService.pesquisarPorCodigoIntegracao(codigoLoja);

        if (dataInicial == null || dataFinal == null) {
            throw new NotificacaoException("O período inicial e final deve ser informado.");
        }

        Empresa empresa = integracao.getEmpresa();
        Operadora operadora = integracao.getOperadora();
        List<AjusteVenda> ajusteVendas = ajusteVendaRepository.buscar(empresa, operadora, dataInicial, dataFinal, integracao.getCodigoIntegracao());

        if (ajusteVendas.isEmpty()) {
            String codigo = integracao.getCodigoIntegracao();
            ajusteVendas = integracaoIfoodService.pesquisarAjusteVendas(codigo, dataInicial, dataFinal);
        }

        return ajusteVendas;
    }
}

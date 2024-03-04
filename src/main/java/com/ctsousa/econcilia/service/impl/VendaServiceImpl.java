package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Integracao;
import com.ctsousa.econcilia.model.Operadora;
import com.ctsousa.econcilia.model.Venda;
import com.ctsousa.econcilia.repository.VendaRepository;
import com.ctsousa.econcilia.service.IntegracaoIfoodService;
import com.ctsousa.econcilia.service.IntegracaoService;
import com.ctsousa.econcilia.service.VendaService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class VendaServiceImpl implements VendaService {

    private final IntegracaoIfoodService integracaoIfoodService;

    private final VendaRepository vendaRepository;

    private final IntegracaoService integracaoService;

    public VendaServiceImpl(IntegracaoIfoodService integracaoIfoodService, VendaRepository vendaRepository, IntegracaoService integracaoService) {
        this.integracaoIfoodService = integracaoIfoodService;
        this.vendaRepository = vendaRepository;
        this.integracaoService = integracaoService;
    }

    @Override
    public List<Venda> buscarVendas(Empresa empresa, Operadora operadora, LocalDate dtInicial, LocalDate dtFinal) {

        if (empresa == null) {
            throw new NotificacaoException("Deve ser informada uma empresa.");
        }

        if (operadora == null) {
            throw new NotificacaoException("Deve ser informada uma operadora.");
        }

        if (dtInicial == null) {
            throw new NotificacaoException("Deve ser informada uma data inicial.");
        }

        if (dtFinal == null) {
            throw new NotificacaoException("Deve ser informada uma data final.");
        }

        List<Venda> vendas = vendaRepository.buscarPor(empresa, operadora, dtInicial, dtFinal);

        if (vendas.isEmpty()) {
            Integracao integracao = integracaoService.pesquisar(empresa, operadora);
            vendas = integracaoIfoodService.pesquisarVendas(integracao.getCodigoIntegracao(), null, null, null, dtInicial, dtFinal);
        }

        return vendas;
    }
}

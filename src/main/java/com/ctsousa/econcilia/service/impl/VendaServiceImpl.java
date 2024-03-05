package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.filter.VendaFilter;
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
    public List<Venda> buscar(Empresa empresa, Operadora operadora, LocalDate dtInicial, LocalDate dtFinal, String metodoPagamento, String bandeira, String tipoRecebimento) {

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

        if (vendas == null || vendas.isEmpty()) {
            Integracao integracao = integracaoService.pesquisar(empresa, operadora);
            vendas = integracaoIfoodService.pesquisarVendas(integracao.getCodigoIntegracao(), dtInicial, dtFinal);
        }

        return filtrarVendas(vendas, metodoPagamento, bandeira, tipoRecebimento);
    }

    private List<Venda> filtrarVendas(final List<Venda> vendas, final String metodoPagamento, final String bandeira, final String tipoRecebimento) {
        return new VendaFilter(vendas, bandeira, metodoPagamento, tipoRecebimento)
                .porBandeira()
                .porMetodoPagamento()
                .porMetodoPagamentoBandeira()
                .porTipoRecebimento()
                .getVendasFiltradas();
    }
}

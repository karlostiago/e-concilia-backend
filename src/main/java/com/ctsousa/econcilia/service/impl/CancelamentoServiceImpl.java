package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.model.*;
import com.ctsousa.econcilia.repository.CancelamentoRepository;
import com.ctsousa.econcilia.repository.OcorrenciaRepository;
import com.ctsousa.econcilia.service.CancelamentoService;
import com.ctsousa.econcilia.service.IntegracaoIfoodService;
import com.ctsousa.econcilia.service.OcorrenciaService;
import com.ctsousa.econcilia.util.StringUtil;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

import static com.ctsousa.econcilia.util.StringUtil.naoTemValor;

@Component
public class CancelamentoServiceImpl implements CancelamentoService {

    private final IntegracaoIfoodService integracaoIfoodService;

    private final CancelamentoRepository cancelamentoRepository;

    public CancelamentoServiceImpl(IntegracaoIfoodService integracaoIfoodService, CancelamentoRepository cancelamentoRepository) {
        this.integracaoIfoodService = integracaoIfoodService;
        this.cancelamentoRepository = cancelamentoRepository;
    }

    @Override
    public List<Cancelamento> buscar(String lojaId, String periodoId) {

        if (Boolean.TRUE.equals(naoTemValor(lojaId))) {
            throw new NotificacaoException("Loja não foi informada.");
        }

        if (Boolean.TRUE.equals(naoTemValor(periodoId))) {
            throw new NotificacaoException("Periodo não foi informado.");
        }

        List<Cancelamento> cancelamentos = cancelamentoRepository.buscar(lojaId, periodoId);

        if (cancelamentos.isEmpty()) {
            cancelamentos = integracaoIfoodService.pesquisarCancelamentos(lojaId, periodoId);
        }

        return cancelamentos;
    }
}

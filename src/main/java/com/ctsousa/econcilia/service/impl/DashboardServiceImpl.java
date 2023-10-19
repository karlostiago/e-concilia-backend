package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.model.dto.DashboardDTO;
import com.ctsousa.econcilia.service.DashboadService;
import com.ctsousa.econcilia.service.IntegracaoService;

import java.time.LocalDate;

public class DashboardServiceImpl implements DashboadService {

    private final IntegracaoService integracaoService;

    public DashboardServiceImpl(IntegracaoService integracaoService) {
        this.integracaoService = integracaoService;
    }

    @Override
    public DashboardDTO carregarInformacoes(String lojaId, LocalDate dtInicial, LocalDate dtFinal) {
        return null;
    }
}

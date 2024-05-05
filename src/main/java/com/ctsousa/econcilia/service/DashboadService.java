package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.model.dto.DashboardDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public interface DashboadService {

    DashboardDTO carregaVendasConsolidadas(final String empresaId, final LocalDate dtInicial, final LocalDate dtFinal);
}

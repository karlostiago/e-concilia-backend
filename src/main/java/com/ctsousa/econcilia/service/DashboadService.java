package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.model.dto.DashboardDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public interface DashboadService {

    DashboardDTO carregarInformacoes(final Long empresaId, final LocalDate dtInicial, final LocalDate dtFinal);
}

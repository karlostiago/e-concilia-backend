package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.model.Venda;
import com.ctsousa.econcilia.model.dto.DashboardDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface DashboadService {


    DashboardDTO carregarInformacoes(final Long empresaId, final LocalDate dtInicial, final LocalDate dtFinal);

    List<Venda> buscarVendasUltimos7Dias(final Long empresaId);
}

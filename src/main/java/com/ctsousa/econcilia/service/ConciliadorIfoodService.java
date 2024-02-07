package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.model.dto.ConciliadorDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public interface ConciliadorIfoodService {

    ConciliadorDTO conciliar(final String codigoLoja, final String metodoPagamento, final String bandeira, final String tipoRecebimento, final LocalDate dtInicial, final LocalDate dtFinal);
}

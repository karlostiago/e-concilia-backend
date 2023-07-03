package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.model.Taxa;
import com.ctsousa.econcilia.model.dto.TaxaDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface TaxaService {

    void validar (TaxaDTO taxaDTO);

    Long calcularTempoExpiracao(LocalDate dataInicial, LocalDate dataFinal);

    void verificaDuplicidade(List<Taxa> taxas);

    void validaEntraEmVigor(List<Taxa> taxas);
}

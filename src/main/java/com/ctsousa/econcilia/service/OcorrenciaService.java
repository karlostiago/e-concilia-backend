package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.model.Integracao;
import com.ctsousa.econcilia.model.Ocorrencia;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface OcorrenciaService {

    List<Ocorrencia> buscar(Integracao integracao, LocalDate dataInicial, LocalDate dataFinal);
}

package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.model.Importacao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ImportacaoService {

    Importacao agendar(final Importacao importacao);

    List<Importacao> buscarPorSituacaoAgendada();
}

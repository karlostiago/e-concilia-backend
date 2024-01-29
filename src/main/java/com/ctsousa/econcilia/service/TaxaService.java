package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.model.Taxa;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface TaxaService {

    void validar(Taxa taxa);

    Long calcularTempoExpiracao(LocalDate dataInicial, LocalDate dataFinal);

    void verificaDuplicidade(List<Taxa> taxas);

    void validaEntraEmVigor(List<Taxa> taxas);

    List<Taxa> buscarPorContrato(final Long contratoId);

    List<Taxa> buscarPorOperadora(final Long operadoraId);

    List<Taxa> buscarPorEmpresa(final Long empresaId);

    List<Taxa> buscarTodos();

    Taxa ativar(Long id);

    Taxa desativar(Long id);

    Taxa pesquisarPorId(Long id);
}

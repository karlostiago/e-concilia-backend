package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Operadora;
import com.ctsousa.econcilia.model.Taxa;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TaxaService extends GeradorRelatorioCSVService {

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

    Taxa buscarPorDescricaoEmpresa(String descricao, Empresa empresa);

    Taxa buscarPor(Empresa empresa, Operadora operadora, String descricao, BigDecimal valor);
}

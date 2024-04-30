package com.ctsousa.econcilia.service;

import java.time.LocalDate;
import java.util.List;

@FunctionalInterface
public interface LocalizadorRegistroIfood<T> {

    List<T> executar(final String codigo, final LocalDate dataInicial, final LocalDate dataFinal);
}

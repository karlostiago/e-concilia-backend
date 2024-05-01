package com.ctsousa.econcilia.util;

import com.ctsousa.econcilia.enumaration.Faixa;
import com.ctsousa.econcilia.model.dto.PeriodoDTO;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

public final class DataUtil {

    private DataUtil() {
    }

    public static LocalDate paraLocalDate(String data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(data, formatter);
    }

    public static String diaMes(final LocalDate data) {
        if (data == null) return null;

        var mes = data.getMonth().getValue() < 10 ? "0" + data.getMonth().getValue() : data.getMonth().getValue();
        var dia = data.getDayOfMonth() < 10 ? "0" + data.getDayOfMonth() : data.getDayOfMonth();
        return dia + "/" + mes;
    }

    public static List<PeriodoDTO> obterPeriodoPorMesFechado(LocalDate dataInicial, LocalDate dataFinal) {
        List<PeriodoDTO> periodos = new ArrayList<>();

        LocalDate dataTemporaria = dataInicial;

        while (!dataTemporaria.isAfter(dataFinal)) {
            LocalDate dtPrimeiroDia = LocalDate.of(dataTemporaria.getYear(), dataTemporaria.getMonth(), 1);
            LocalDate dtUltimoDiaMes = dtPrimeiroDia.withDayOfMonth(dtPrimeiroDia.lengthOfMonth());
            periodos.add(new PeriodoDTO(dtPrimeiroDia, dtUltimoDiaMes));
            dataTemporaria = dtPrimeiroDia.plusMonths(1);
        }

        if (!periodos.isEmpty()) {
            PeriodoDTO ultimoPeriodo = periodos.get(periodos.size() - 1);
            periodos.set(periodos.size() - 1, new PeriodoDTO(ultimoPeriodo.getDe(), dataFinal));
        }

        return periodos;
    }

    public static List<PeriodoDTO> periodoAnual(LocalDate dataInicial, Faixa faixa) {
        LocalDate dtInicial = dataInicial.minusYears(1).withDayOfMonth(1);
        LocalDate dtFinal = dataInicial.withDayOfMonth(dataInicial.lengthOfMonth());
        return calcularPeriodos(dtInicial, dtFinal, faixa);
    }

    public static YearMonth parseMesAno(String mesAno) {
        String [] periodo = mesAno.split("/");

        Map<String, Month> mesMap = new HashMap<>();
        for (Month month : Month.values()) {
            String mesAbreviado = month.getDisplayName(java.time.format.TextStyle.SHORT, Locale.forLanguageTag("pt-BR"));
            mesAbreviado = mesAbreviado.substring(0, 1).toUpperCase() + mesAbreviado.substring(1);
            mesAbreviado = mesAbreviado.replace(".", "");
            mesMap.put(mesAbreviado, month);
        }

        String mesStr = periodo[0];
        int ano = Integer.parseInt(periodo[1]);

        Month mes = mesMap.get(mesStr);

        return YearMonth.of(ano, mes);
    }

    public static String paraPtBr(final LocalDate data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return data.format(formatter);
    }

    public static boolean ultimoDiaMes(final LocalDate data) {
        int diaAtual = data.getDayOfMonth();
        int ultimoDiaMes = data.lengthOfMonth();
        return diaAtual == ultimoDiaMes;
    }

    public static LocalDate getUltimoDiaMes(final LocalDate data) {
        LocalDate primeiroDiaProximoMes = data.plusMonths(1);
        return primeiroDiaProximoMes.minusDays(1);
    }

    public static LocalDate getPrimeiroDiaMes(final LocalDate data) {
        return data.withDayOfMonth(1);
    }

    private static List<PeriodoDTO> calcularPeriodos(LocalDate dtInicial, LocalDate dtFinal, Faixa faixa) {
        List<PeriodoDTO> periodos = new ArrayList<>();
        LocalDate dtInicio = dtInicial;
        LocalDate dtFim;

        while (dtInicio.isBefore(dtFinal)) {
            dtFim = dtInicio.plusMonths(faixa.getMeses()).withDayOfMonth(1).minusDays(1);

            if (dtFim.isAfter(dtFinal)) {
                dtFim = dtFinal;
            }
            periodos.add(new PeriodoDTO(dtInicio, dtFim));
            dtInicio = dtFim.plusDays(1);
        }

        return periodos;
    }
}

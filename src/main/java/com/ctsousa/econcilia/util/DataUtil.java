package com.ctsousa.econcilia.util;

import com.ctsousa.econcilia.enumaration.Faixa;
import com.ctsousa.econcilia.model.dto.PeriodoDTO;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

public final class DataUtil {

    private static Map<Integer, Month> mes = new HashMap<>();

    static {
        mes.put(1, Month.JANUARY);
        mes.put(2, Month.FEBRUARY);
        mes.put(3, Month.MARCH);
        mes.put(4, Month.APRIL);
        mes.put(5, Month.MAY);
        mes.put(6, Month.JUNE);
        mes.put(7, Month.JULY);
        mes.put(8, Month.AUGUST);
        mes.put(9, Month.SEPTEMBER);
        mes.put(10, Month.OCTOBER);
        mes.put(11, Month.NOVEMBER);
        mes.put(12, Month.DECEMBER);
    }

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

    public static List<PeriodoDTO> periodos(LocalDate periodoInicial, long totalDias) {
        boolean executarCalculo = true;
        List<PeriodoDTO> periodos = new ArrayList<>();

        if (totalDias <= 30) {
            periodos.add(new PeriodoDTO(periodoInicial, periodoInicial.plusDays(totalDias)));
            executarCalculo = false;
        }

        while (executarCalculo) {
            LocalDate periodoFinal = periodoInicial.plusDays(30);

            if (!periodos.isEmpty()) {
                periodoInicial = periodos.get(periodos.size() - 1).getAte()
                        .plusDays(1);

                periodoFinal = periodoInicial.plusDays(totalDias < 30 ? totalDias : 30)
                        .minusDays(1);
            }

            periodos.add(new PeriodoDTO(periodoInicial, periodoFinal));
            totalDias -= 30;

            if (totalDias <= 0) {
                executarCalculo = false;
            }
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

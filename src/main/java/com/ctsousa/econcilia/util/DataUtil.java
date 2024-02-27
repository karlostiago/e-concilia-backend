package com.ctsousa.econcilia.util;

import com.ctsousa.econcilia.model.dto.PeriodoDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class DataUtil {

    private DataUtil() {
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
}

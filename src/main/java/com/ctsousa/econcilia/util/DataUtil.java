package com.ctsousa.econcilia.util;

import java.time.LocalDate;

public final class DataUtil {

    private DataUtil() {
    }

    public static String diaMes(final LocalDate data) {
        if (data == null) return null;

        var mes = data.getMonth().getValue() < 10 ? "0" + data.getMonth().getValue() : data.getMonth().getValue();
        var dia = data.getDayOfMonth() < 10 ? "0" + data.getDayOfMonth() : data.getDayOfMonth();
        return dia + "/" + mes;
    }
}

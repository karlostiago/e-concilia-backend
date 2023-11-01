package com.ctsousa.econcilia.util;

import java.time.LocalDate;

public final class DataUtil {

    private DataUtil() { }

    public static String mesAno(final LocalDate data) {
        return data.getDayOfMonth() + "/" + data.getMonth().getValue();
    }
}

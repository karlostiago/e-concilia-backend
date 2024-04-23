package com.ctsousa.econcilia.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class DecimalUtil {

    private DecimalUtil () { }

    public static boolean iqualZero(BigDecimal valor) {
        List<String> zeros = List.of("0.0", "0.00", "0.000", "0.0000", "0.00000", "0.000000", "0.0000000", "0.00000000", "0.000000000", "0.0000000000");

        for (String zero : zeros) {
            if (valor.equals(new BigDecimal(zero))) return true;
        }

        return false;
    }

    public static String monetarioPtBr(final BigDecimal valor) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("pt", "BR"));
        symbols.setCurrencySymbol("");

        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", symbols);
        return decimalFormat.format(valor);
    }
}

package com.ctsousa.econcilia.util;

import java.math.BigDecimal;
import java.util.List;

public final class CalculadoraUtil {

    private CalculadoraUtil() {
    }

    public static BigDecimal somar(List<BigDecimal> valores) {
        if (valores == null) return BigDecimal.valueOf(0D);

        return valores
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal multiplicar(BigDecimal valor, int fatorMultiplicador) {
        if (valor == null) return BigDecimal.valueOf(0D);

        return valor.multiply(BigDecimal.valueOf(fatorMultiplicador));
    }
}

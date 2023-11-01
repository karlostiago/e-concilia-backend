package com.ctsousa.econcilia.util;

import java.math.BigDecimal;
import java.util.List;

public final class CalculadoraUtil {

    private CalculadoraUtil() { }

    public static BigDecimal somar(List<BigDecimal> valores) {
        return valores
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

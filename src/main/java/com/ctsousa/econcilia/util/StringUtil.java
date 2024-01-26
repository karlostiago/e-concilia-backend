package com.ctsousa.econcilia.util;

public final class StringUtil {

    private static final String UNDEFINED = "undefined";
    private static final String NULL = "null";

    private StringUtil () { }

    public static String somenteNumero(final String str) {
        if (str == null) {
            return null;
        }
        return str.replaceAll("[^0-9]", "");
    }

    public static String maiuscula(final String str) {
        if (str == null) {
            return null;
        }
        return str.toUpperCase();
    }

    public static Boolean temValor(final String str) {
        return str != null && !str.isEmpty() && !UNDEFINED.equalsIgnoreCase(str) && !NULL.equalsIgnoreCase(str);
    }

    public static Boolean naoTemValor(final String str) {
        return !temValor(str);
    }
}

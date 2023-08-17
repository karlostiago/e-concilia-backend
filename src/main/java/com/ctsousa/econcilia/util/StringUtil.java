package com.ctsousa.econcilia.util;

public final class StringUtil {

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
}

package com.ctsousa.econcilia.util;

import java.text.Normalizer;
import java.util.regex.Pattern;

public final class StringUtil {

    private static final String UNDEFINED = "undefined";
    private static final String NULL = "null";

    private StringUtil() {
    }

    public static String somenteNumero(final String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        return str.replaceAll("\\D", "");
    }

    public static String maiuscula(final String str) {
        if (str == null || str.isEmpty()) {
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

    public static String removeCaracteresEspeciais(final String str) {
        String textoNormalizado = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(textoNormalizado).replaceAll("");
    }
}

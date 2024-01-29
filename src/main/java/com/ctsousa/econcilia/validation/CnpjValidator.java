package com.ctsousa.econcilia.validation;

import com.ctsousa.econcilia.exceptions.NotificacaoException;

public class CnpjValidator {

    private final String cnpj;

    public CnpjValidator(final String cnpj) {
        this.cnpj = cnpj;
    }

    public void validar() {
        final var primeiroDigito = calcularPrimeiroDigito();
        final var segundoDigito = calcularSegundoDigito(primeiroDigito);

        final var digitoCalculado = primeiroDigito + segundoDigito.toString();
        final var digitoExtraido = this.cnpj.substring(this.cnpj.length() - 2);

        if (!digitoCalculado.equals(digitoExtraido)) {
            throw new NotificacaoException(String.format("Cnpj %s inválido.", this.cnpj));
        }
    }

    private Integer calcularPrimeiroDigito() {
        int[] algarismos = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        return calcularDigito(algarismos, null);
    }

    private Integer calcularSegundoDigito(final Integer digitoAdicional) {
        int[] algarismos = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        return calcularDigito(algarismos, digitoAdicional);
    }

    private int calcularDigito(int[] algarismos, Integer digitoAdicional) {
        var cnpj = this.cnpj.substring(0, this.cnpj.length() - 2);
        var total = 0;
        var contador = 0;

        if (digitoAdicional != null) {
            cnpj = cnpj.concat(digitoAdicional.toString());
        }

        for (char algarismo : cnpj.toCharArray()) {
            total += algarismos[contador] * Integer.parseInt(Character.toString(algarismo));
            contador++;
        }

        var digito = 0;
        final var modulo = 11;
        final var resultado = total % modulo;

        if (resultado >= 2) {
            digito = modulo - resultado;
        }

        return digito;
    }
}
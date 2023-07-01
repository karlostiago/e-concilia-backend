package com.ctsousa.econcilia.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class CnpjException extends RuntimeException {

    public CnpjException(final String cnpj) {
        super(String.format("Cnpj '%s' não é válido.", cnpj));
    }
}

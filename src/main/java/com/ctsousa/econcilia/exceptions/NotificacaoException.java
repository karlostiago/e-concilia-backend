package com.ctsousa.econcilia.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class NotificacaoException extends RuntimeException {

    public NotificacaoException(final String mensagem) {
        super(mensagem);
    }
}

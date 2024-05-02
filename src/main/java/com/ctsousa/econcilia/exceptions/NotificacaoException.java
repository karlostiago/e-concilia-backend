package com.ctsousa.econcilia.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class NotificacaoException extends RuntimeException {

    private final Severidade severidade;

    public NotificacaoException(final String mensagem) {
        super(mensagem);
        this.severidade = Severidade.ERRO;
    }

    public NotificacaoException(final String mensagem, final Severidade severidade) {
        super(mensagem);
        this.severidade = severidade;
    }
}

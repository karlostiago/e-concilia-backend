package com.ctsousa.econcilia.exceptions.handler;

import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.exceptions.builder.ErrorBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@RestControllerAdvice
public class NotificacaoExceptonHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({NotificacaoException.class})
    public ResponseEntity<List<Error>> handleNotificacaoException(NotificacaoException ex) {
        List<Error> erros = List.of((ErrorBuilder.builder()
                .comStatus(HttpStatus.BAD_REQUEST)
                .comMensagem(ex.getMessage())
                .comDetalhe(ex)
                .build()));
        return ResponseEntity.badRequest().body(erros);
    }
}

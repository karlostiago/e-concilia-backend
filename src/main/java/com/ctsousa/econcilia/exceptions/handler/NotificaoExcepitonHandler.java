package com.ctsousa.econcilia.exceptions.handler;

import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.exceptions.builder.ErrorBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestControllerAdvice
public class NotificaoExcepitonHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler( { NotificacaoException.class } )
    public ResponseEntity<List<Error>> handleNotificacaoException (NotificacaoException ex) {
        List<Error> erros = List.of((ErrorBuilder.builder()
                .comStatus(HttpStatus.BAD_REQUEST)
                .comMensagem(ex.getMessage())
                .comDetalhe(ex)
                .build()));
        return ResponseEntity.badRequest().body(erros);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<Error> erros = criarListaDeErros(ex, ex.getBindingResult());
        return handleExceptionInternal(ex,erros, headers, status, request);
    }

    private List<Error> criarListaDeErros(Exception ex, BindingResult bindingResult) {
        List<Error> erros = new ArrayList<>();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            erros.add(ErrorBuilder.builder()
                    .comStatus(HttpStatus.BAD_REQUEST)
                    .comMensagem(fieldError.getDefaultMessage())
                    .comDetalhe(ex.getCause())
                    .build());
        }

        return erros;
    }
}

package com.projectmanager.backend.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

    public RecursoNaoEncontradoException(String recurso, Long id) {
        super(recurso + " com ID " + id + " não encontrado.");
    }

    public RecursoNaoEncontradoException(String recurso, String identificador) {
        super(recurso + " com identificador '" + identificador + "' não encontrado.");
    }
}
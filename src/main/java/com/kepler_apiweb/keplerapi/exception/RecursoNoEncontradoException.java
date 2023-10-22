package com.kepler_apiweb.keplerapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus (value = HttpStatus.NOT_FOUND)
public class RecursoNoEncontradoException extends RuntimeException {
    private static final long serialVersionUID = 1l;
    public RecursoNoEncontradoException(String mensaje){
        super(mensaje);
    }
}

package com.kepler_apiweb.keplerapi.controller;

import com.apiweb.exception.CamposInvalidosException;
import com.kepler_apiweb.keplerapi.exception.RecursoNoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ManejadorExcepcionesPersonalizadas {

    @ExceptionHandler (RecursoNoEncontradoException.class)
    public ResponseEntity<String> handleResourceNotFoundException (RecursoNoEncontradoException ex){
        //obtenemos el mensaje personalizado creado en los metodos del controller
        String mensaje = ex.getMessage();

        return  new ResponseEntity<>(mensaje, HttpStatus.NOT_FOUND);

    }
    @ExceptionHandler (CamposInvalidosException.class)
    public ResponseEntity<String> handleCamposInvalidosException(CamposInvalidosException ex){
        String mensaje = ex.getMessage();
        return  new ResponseEntity<>(mensaje, HttpStatus.BAD_REQUEST);

    }
}

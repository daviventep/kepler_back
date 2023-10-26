package com.kepler_apiweb.keplerapi.controller;


import com.kepler_apiweb.keplerapi.exception.InvalidFieldsException;
import com.kepler_apiweb.keplerapi.exception.ResourceExist;
import com.kepler_apiweb.keplerapi.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ManejadorExcepcionesPersonalizadas {

    @ExceptionHandler (ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException (ResourceNotFoundException ex){
        //obtenemos el mensaje personalizado creado en los metodos del controller
        String message = ex.getMessage();
        return  new ResponseEntity<>(message, HttpStatus.NOT_FOUND);

    }
    @ExceptionHandler (InvalidFieldsException.class)
    public ResponseEntity<String> handleCamposInvalidosException(InvalidFieldsException ex){
        String message = ex.getMessage();
        return  new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);

    }
    @ExceptionHandler (ResourceExist.class)
    public ResponseEntity<String> handleRecursoExistenteException(ResourceExist ex){
        String message = ex.getMessage();
        return  new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);

    }
}

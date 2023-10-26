package com.kepler_apiweb.keplerapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidFieldsException extends RuntimeException{
    public InvalidFieldsException(String message){
        super(message);
    }
}

package com.kepler_apiweb.keplerapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus (value = HttpStatus.CONFLICT)
public class ResourceExist extends RuntimeException {
    private static final long serialVersionUID = 1l;
    public ResourceExist(String message){
        super(message);
    }
}

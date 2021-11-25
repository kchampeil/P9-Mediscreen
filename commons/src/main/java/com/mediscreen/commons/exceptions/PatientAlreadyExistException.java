package com.mediscreen.commons.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class PatientAlreadyExistException extends Exception {
    public PatientAlreadyExistException(String message) {
        super(message);
    }
}

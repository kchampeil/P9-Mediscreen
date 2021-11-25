package com.mediscreen.commons.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PatientDoesNotExistException extends Exception {
    public PatientDoesNotExistException(String message) {
        super(message);
    }
}

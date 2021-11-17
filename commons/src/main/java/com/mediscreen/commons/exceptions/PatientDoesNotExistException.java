package com.mediscreen.commons.exceptions;

public class PatientDoesNotExistException extends Exception {
    public PatientDoesNotExistException(String message) {
        super(message);
    }
}

package com.mediscreen.patient.exceptions;

public class PatientDoesNotExistException extends Exception {
    public PatientDoesNotExistException(String message) {
        super(message);
    }
}

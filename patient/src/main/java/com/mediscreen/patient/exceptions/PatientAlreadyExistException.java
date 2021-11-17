package com.mediscreen.patient.exceptions;

public class PatientAlreadyExistException extends Exception {
    public PatientAlreadyExistException(String message) {
        super(message);
    }
}

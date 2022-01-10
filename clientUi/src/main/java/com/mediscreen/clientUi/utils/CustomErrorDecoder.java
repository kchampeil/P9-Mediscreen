package com.mediscreen.clientUi.utils;

import com.mediscreen.clientUi.proxies.INoteProxy;
import com.mediscreen.commons.constants.ExceptionConstants;
import com.mediscreen.commons.exceptions.NoteDoesNotExistException;
import com.mediscreen.commons.exceptions.PatientAlreadyExistException;
import com.mediscreen.commons.exceptions.PatientDoesNotExistException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;


public class CustomErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String invoker, Response response) {

        /* identifying the client name */
        String parts[] = invoker.split("#");

        /* DoesNotExist */
        if (response.status() == HttpStatus.NOT_FOUND.value()) {
            if (INoteProxy.class.getName().contains(parts[0])) {
                return new NoteDoesNotExistException(ExceptionConstants.NOTE_NOT_FOUND);
            } else {
                return new PatientDoesNotExistException(ExceptionConstants.PATIENT_NOT_FOUND);
            }
        }

        /* AlreadyExist */
        if (response.status() == HttpStatus.CONFLICT.value()) {
            return new PatientAlreadyExistException(ExceptionConstants.PATIENT_ALREADY_EXISTS);
        }

        return defaultErrorDecoder.decode(invoker, response);
    }

}

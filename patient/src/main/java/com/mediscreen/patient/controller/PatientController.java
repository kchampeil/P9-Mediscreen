package com.mediscreen.patient.controller;

import java.util.List;
import java.util.Optional;

import com.mediscreen.commons.constants.ExceptionConstants;
import com.mediscreen.commons.dto.PatientDTO;
import com.mediscreen.commons.exceptions.PatientAlreadyExistException;
import com.mediscreen.commons.exceptions.PatientDoesNotExistException;
import com.mediscreen.patient.constants.LogConstants;
import com.mediscreen.patient.service.contracts.IPatientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Api(value = "API for CRUD operations on patients")
@RestController
@RequestMapping("patient")
public class PatientController {

    private final IPatientService patientService;

    @Autowired
    public PatientController(IPatientService patientService) {
        this.patientService = patientService;
    }

    @ApiOperation(value = "Get all registered patients")
    @GetMapping(value = "list")
    public List<PatientDTO> getAllPatients() {
        log.debug(LogConstants.GET_ALL_PATIENTS_REQUEST_RECEIVED);

        return patientService.getAllPatients();
    }

    @ApiOperation(value = "Get patient by id")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Patient found"),
        @ApiResponse(code = 404, message = ExceptionConstants.PATIENT_NOT_FOUND)
    })
    @GetMapping(value = "get")
    public ResponseEntity<PatientDTO> getPatientById(@RequestParam Integer patientId) {

        log.debug(LogConstants.GET_PATIENT_BY_ID_REQUEST_RECEIVED, patientId);

        try {
            PatientDTO patientDTO = patientService.getPatientById(patientId);

            log.debug(LogConstants.GET_PATIENT_BY_ID_REQUEST_OK, patientId);
            return new ResponseEntity<>(patientDTO, HttpStatus.OK);

        } catch (PatientDoesNotExistException patientDoesNotExistException) {
            log.error(patientDoesNotExistException.getMessage() + " \n");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, patientDoesNotExistException.getMessage(),
                                              patientDoesNotExistException);
        }
    }

    @ApiOperation(value = "Update patient")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Patient updated"),
        @ApiResponse(code = 404, message = ExceptionConstants.PATIENT_NOT_FOUND),
        @ApiResponse(code = 409, message = ExceptionConstants.PATIENT_ALREADY_EXISTS)
    })
    @PutMapping(value = "update", consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PatientDTO> updatePatient(@RequestBody PatientDTO patientDtoToUpdate) {

        log.debug(LogConstants.UPDATE_PATIENT_REQUEST_RECEIVED, patientDtoToUpdate.getId());

        try {
            Optional<PatientDTO> updatedPatientDto = patientService.updatePatient(patientDtoToUpdate);

            if (updatedPatientDto.isPresent()) {
                log.info(LogConstants.UPDATE_PATIENT_REQUEST_OK, patientDtoToUpdate.getId());
                return new ResponseEntity<>(updatedPatientDto.get(), HttpStatus.OK);
            } else {
                log.error(LogConstants.UPDATE_PATIENT_REQUEST_KO, patientDtoToUpdate.getId());
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        } catch (PatientDoesNotExistException patientDoesNotExistException) {
            log.error(patientDoesNotExistException.getMessage() + " \n");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, patientDoesNotExistException.getMessage());

        } catch (PatientAlreadyExistException patientAlreadyExistException) {
            log.error(patientAlreadyExistException.getMessage() + " \n");
            throw new ResponseStatusException(HttpStatus.CONFLICT, patientAlreadyExistException.getMessage());
        }
    }

    @ApiOperation(value = "Add patient")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Patient created"),
        @ApiResponse(code = 409, message = ExceptionConstants.PATIENT_ALREADY_EXISTS)
    })
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PatientDTO> addPatient(@RequestBody PatientDTO patientDtoToAdd) {

        log.debug(LogConstants.ADD_PATIENT_REQUEST_RECEIVED,
                  patientDtoToAdd.getFirstname() + " " + patientDtoToAdd.getLastname());

        try {
            Optional<PatientDTO> addedPatient = patientService.addPatient(patientDtoToAdd);

            if (addedPatient.isPresent()) {
                log.info(LogConstants.ADD_PATIENT_REQUEST_OK, addedPatient.get().getId());
                return new ResponseEntity<>(addedPatient.get(), HttpStatus.CREATED);
            } else {
                log.error(LogConstants.ADD_PATIENT_REQUEST_KO);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        } catch (PatientAlreadyExistException patientAlreadyExistException) {
            log.error(patientAlreadyExistException.getMessage() + " \n");
            throw new ResponseStatusException(HttpStatus.CONFLICT, patientAlreadyExistException.getMessage());
        }
    }

    @ApiOperation(value = "Delete patient")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Patient deleted"),
        @ApiResponse(code = 404, message = ExceptionConstants.PATIENT_NOT_FOUND)
    })
    @DeleteMapping(value = "/delete")
    public ResponseEntity<Integer> deletePatientById(@RequestParam Integer patientId) {

        log.debug(LogConstants.DELETE_PATIENT_BY_ID_REQUEST_RECEIVED, patientId);

        try {
            patientService.deletePatientById(patientId);

            log.debug(LogConstants.DELETE_PATIENT_BY_ID_REQUEST_OK, patientId);
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (PatientDoesNotExistException patientDoesNotExistException) {
            log.error(patientDoesNotExistException.getMessage() + " \n");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, patientDoesNotExistException.getMessage(),
                                              patientDoesNotExistException);
        }
    }
}

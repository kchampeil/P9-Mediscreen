package com.mediscreen.patient.controller;

import static com.mediscreen.commons.dto.PatientDTO.PATIENT_DTO_EXAMPLE;

import com.mediscreen.commons.constants.ExceptionConstants;
import com.mediscreen.commons.dto.PatientDTO;
import com.mediscreen.commons.exceptions.PatientAlreadyExistException;
import com.mediscreen.commons.exceptions.PatientDoesNotExistException;
import com.mediscreen.patient.constants.LogConstants;
import com.mediscreen.patient.service.contracts.IPatientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Api(value = "API for CRUD operations on patients")
@RestController
@RequestMapping("/patient")
public class PatientController {

    private final IPatientService patientService;

    @Autowired
    public PatientController(IPatientService patientService) {
        this.patientService = patientService;
    }

    @ApiOperation(
        value = "Get all registered patients by page for a given number of items per page, " +
                "sorted by one field in one direction")
    @GetMapping(value = "/list")
    public Page<PatientDTO> getAllPatientsByPage(@RequestParam int pageNumber, @RequestParam int itemsPerPage,
                                                 @RequestParam String sortField, @RequestParam String sortDir) {
        log.debug(LogConstants.GET_ALL_PATIENTS_REQUEST_RECEIVED);

        return patientService.getAllPatientsPageable(pageNumber, itemsPerPage, sortField, sortDir);
    }

    @ApiOperation(value = "Get patient by id")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Patient found"),
        @ApiResponse(code = 404, message = ExceptionConstants.PATIENT_NOT_FOUND)
    })
    @GetMapping(value = "/")
    public ResponseEntity<PatientDTO> getPatientById(
        @ApiParam(name = "id", type = "Integer", value = "id of patient", example = "1", required = true)
        @RequestParam Integer patientId) {

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
        @ApiResponse(code = 200, message = "Patient updated",
                     examples = @Example(value = {@ExampleProperty(mediaType = "application/json",
                                                                   value = PATIENT_DTO_EXAMPLE)})),
        @ApiResponse(code = 404, message = ExceptionConstants.PATIENT_NOT_FOUND),
        @ApiResponse(code = 409, message = ExceptionConstants.PATIENT_ALREADY_EXISTS)
    })
    @PutMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PatientDTO> updatePatient(@ApiParam(value = "Patient information to update")
                                                    @RequestBody PatientDTO patientDtoToUpdate) {

        log.debug(LogConstants.UPDATE_PATIENT_REQUEST_RECEIVED, patientDtoToUpdate.getId());

        try {
            return patientService.updatePatient(patientDtoToUpdate)
                                 .map(patientDtoUpdated -> new ResponseEntity<>(patientDtoUpdated, HttpStatus.OK))
                                 .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));

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
        @ApiResponse(code = 201, message = "Patient created", examples = @Example(value =
            {@ExampleProperty(mediaType = "application/json",
                              value = PATIENT_DTO_EXAMPLE)})),
        @ApiResponse(code = 409, message = ExceptionConstants.PATIENT_ALREADY_EXISTS)
    })
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PatientDTO> addPatient(@ApiParam(value = "Information about the patient to create")
                                                 @RequestBody PatientDTO patientDtoToAdd) {

        log.debug(LogConstants.ADD_PATIENT_REQUEST_RECEIVED,
                  patientDtoToAdd.getFirstname() + " " + patientDtoToAdd.getLastname());

        try {
            return patientService.addPatient(patientDtoToAdd)
                                 .map(patientDtoAdded -> new ResponseEntity<>(patientDtoAdded, HttpStatus.CREATED))
                                 .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        } catch (PatientAlreadyExistException patientAlreadyExistException) {
            log.error(patientAlreadyExistException.getMessage() + " \n");
            throw new ResponseStatusException(HttpStatus.CONFLICT, patientAlreadyExistException.getMessage());
        }
    }

    @ApiOperation(value = "Delete patient by id")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Patient deleted"),
        @ApiResponse(code = 404, message = ExceptionConstants.PATIENT_NOT_FOUND)
    })
    @DeleteMapping(value = "/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Integer> deletePatientById(
        @ApiParam(name = "id", type = "Integer", value = "id of patient", example = "1", required = true)
        @RequestParam Integer patientId) {

        log.debug(LogConstants.DELETE_PATIENT_BY_ID_REQUEST_RECEIVED, patientId);

        try {
            patientService.deletePatientById(patientId);

            log.debug(LogConstants.DELETE_PATIENT_BY_ID_REQUEST_OK, patientId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        } catch (PatientDoesNotExistException patientDoesNotExistException) {
            log.error(patientDoesNotExistException.getMessage() + " \n");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, patientDoesNotExistException.getMessage(),
                                              patientDoesNotExistException);
        }
    }
}

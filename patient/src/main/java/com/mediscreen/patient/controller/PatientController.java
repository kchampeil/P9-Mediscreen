package com.mediscreen.patient.controller;

import java.util.List;
import java.util.Optional;

import com.mediscreen.patient.constants.LogConstants;
import com.mediscreen.patient.dto.PatientDTO;
import com.mediscreen.patient.exceptions.PatientAlreadyExistException;
import com.mediscreen.patient.exceptions.PatientDoesNotExistException;
import com.mediscreen.patient.service.contracts.IPatientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    @GetMapping(value = "get")
    public ResponseEntity<PatientDTO> getPatientById(@RequestParam Integer patientId) {

        log.debug(LogConstants.GET_PATIENT_BY_ID_REQUEST_RECEIVED, patientId);

        try {
            PatientDTO patientDTO = patientService.getPatientById(patientId);

            log.debug(LogConstants.GET_PATIENT_BY_ID_REQUEST_OK, patientId);
            return new ResponseEntity<>(patientDTO, HttpStatus.OK);

        } catch (PatientDoesNotExistException patientDoesNotExistException) {
            log.error(patientDoesNotExistException.getMessage() + " \n");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, patientDoesNotExistException.getMessage());
        }
    }

    @ApiOperation(value = "Update patient")
    @PutMapping(value = "update")
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

        } catch (Exception e) {
            log.error(e.getMessage() + " \n");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

    }

}

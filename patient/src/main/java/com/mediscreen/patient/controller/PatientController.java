package com.mediscreen.patient.controller;

import java.util.List;

import com.mediscreen.patient.constants.LogConstants;
import com.mediscreen.patient.dto.PatientDTO;
import com.mediscreen.patient.service.contracts.IPatientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}

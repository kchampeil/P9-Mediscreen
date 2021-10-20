package com.mesdiscreen.patient.controller;

import java.util.List;

import com.mesdiscreen.patient.constants.LogConstants;
import com.mesdiscreen.patient.dto.PatientDTO;
import com.mesdiscreen.patient.service.contracts.IPatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("patient")
public class PatientController {

    private final IPatientService patientService;

    @Autowired
    public PatientController(IPatientService patientService) {
        this.patientService = patientService;
    }

    //TODO @ApiOperation(value = "Get all registered patients")
    @GetMapping(value = "list")
    public List<PatientDTO> getAllPatients() {
        log.info(LogConstants.GET_ALL_PATIENTS_REQUEST_RECEIVED);

        return patientService.getAllPatients();
    }

}

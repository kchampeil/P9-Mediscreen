package com.mediscreen.riskAssessment.controller;

import com.mediscreen.commons.dto.RiskAssessmentDTO;
import com.mediscreen.riskAssessment.constants.LogConstants;
import com.mediscreen.riskAssessment.service.contracts.IRiskAssessmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Api(value = "API for calculation fo risk disease for patients")
@Controller
@RequestMapping("/assess")
public class RiskAssessmentController {

    private final IRiskAssessmentService riskAssessmentService;

    @Autowired
    public RiskAssessmentController(IRiskAssessmentService riskAssessmentService) {
        this.riskAssessmentService = riskAssessmentService;
    }

    @ApiOperation(value = "Assess diabetes risk for patient")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Diabetes risk evaluated")
    })
    @PostMapping(value = "/diabetes") //TOASK : post et pas get dans l'énoncé
    public ResponseEntity<String> getDiabetesRiskForPatient(
        @ApiParam(value = "information and medical notes about patient")
        @RequestBody RiskAssessmentDTO riskAssessmentDTO) {

        log.debug(LogConstants.GET_RISK_FOR_PATIENT_REQUEST_RECEIVED);

        return new ResponseEntity<>(riskAssessmentService.evaluateRisk(riskAssessmentDTO).getDescription(),
                                    HttpStatus.OK);
    }
}

package com.mediscreen.clientUi.controller;

import java.util.List;

import com.mediscreen.clientUi.constants.LogConstants;
import com.mediscreen.clientUi.constants.ViewNameConstants;
import com.mediscreen.clientUi.model.PatientDTO;
import com.mediscreen.clientUi.proxies.IPatientProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class OrganizerController {

    private final IPatientProxy patientProxy;

    @Autowired
    public OrganizerController(IPatientProxy patientProxy) {
        this.patientProxy = patientProxy;
    }

    @GetMapping("/patient/list")
    public String showAllPatients(Model model) {

        log.info(LogConstants.SHOW_ALL_PATIENTS_REQUEST_RECEIVED);

        model.addAttribute("patientDtoList", patientProxy.getAllPatients());

        return ViewNameConstants.SHOW_ALL_PATIENTS;
    }
}

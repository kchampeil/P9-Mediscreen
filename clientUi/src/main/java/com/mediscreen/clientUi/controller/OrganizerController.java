package com.mediscreen.clientUi.controller;

import static com.mediscreen.clientUi.utils.MessageUtil.formatOutputMessage;

import com.mediscreen.clientUi.constants.LogConstants;
import com.mediscreen.clientUi.constants.ViewNameConstants;
import com.mediscreen.clientUi.model.PatientDTO;
import com.mediscreen.clientUi.proxies.IPatientProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping("patient/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer patientId, Model model,
                                 RedirectAttributes redirectAttributes) {

        log.info(LogConstants.SHOW_UPDATE_FORM_RECEIVED, patientId);

        ResponseEntity<PatientDTO> patientDTOResponseEntity = patientProxy.getPatientById(patientId);

        if (patientDTOResponseEntity.getStatusCode() == HttpStatus.OK) {
            model.addAttribute("patient", patientDTOResponseEntity.getBody());
            return ViewNameConstants.UPDATE_PATIENT;

        } else {
            redirectAttributes.addFlashAttribute("errorMessage",
                                                 formatOutputMessage("patient.not.found", patientId.toString()));
            return "redirect:patient/list"; //TODO ? "redirect:"+ViewNameConstants.SHOW_ALL_PATIENTS
        }
        /*TOASK
        try {
            model.addAttribute("patient", patientProxy.getPatientById(patientId));
            return ViewNameConstants.UPDATE_PATIENT;

        } catch (IllegalArgumentException illegalArgumentException) {
            log.error(illegalArgumentException.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage",
                                                 formatOutputMessage("patient.is.not.valid", patientId.toString()));
            return "redirect:patient/list"; //TODO ? "redirect:"+ViewNameConstants.SHOW_ALL_PATIENTS
        }

         */

    }
}

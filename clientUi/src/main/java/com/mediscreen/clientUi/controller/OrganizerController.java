package com.mediscreen.clientUi.controller;

import static com.mediscreen.clientUi.utils.MessageUtil.formatOutputMessage;

import javax.validation.Valid;

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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
            return "redirect:" + ViewNameConstants.SHOW_ALL_PATIENTS;
        }
        /*TODO
        try {
            model.addAttribute("patient", patientProxy.getPatientById(patientId));
            return ViewNameConstants.UPDATE_PATIENT;

        } catch (IllegalArgumentException illegalArgumentException) {
            log.error(illegalArgumentException.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage",
                                                 formatOutputMessage("patient.not.found", patientId.toString()));
            return "redirect:" + ViewNameConstants.SHOW_ALL_PATIENTS;
        }

         */

    }

    @PostMapping("patient/update/{id}")
    public String updatePatient(@PathVariable("id") Integer patientId,
                                @ModelAttribute("patient") @Valid PatientDTO patientDTO,
                                BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        log.info(LogConstants.UPDATE_PATIENT_RECEIVED, patientId);

        if (result.hasErrors()) {
            log.error(LogConstants.UPDATE_PATIENT_REQUEST_NOT_VALID + patientId + "\n");
            return ViewNameConstants.UPDATE_PATIENT;
        }

        try {
            patientProxy.updatePatient(patientDTO);

            log.info(LogConstants.UPDATE_PATIENT_REQUEST_OK, patientId);

            redirectAttributes.addFlashAttribute("infoMessage",
                                                 formatOutputMessage("patient.update.ok", patientId.toString()));
            return "redirect:" + ViewNameConstants.SHOW_ALL_PATIENTS;

        } catch (Exception exception) {
            log.error(LogConstants.UPDATE_PATIENT_REQUEST_KO, patientId, exception.getMessage());
            model.addAttribute("errorMessage",
                               formatOutputMessage("patient.update.ko",
                                                   patientId.toString() + ": " + exception.getMessage()));
            return ViewNameConstants.UPDATE_PATIENT;
        }
    }

}

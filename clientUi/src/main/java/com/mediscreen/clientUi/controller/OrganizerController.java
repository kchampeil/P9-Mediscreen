package com.mediscreen.clientUi.controller;

import static com.mediscreen.clientUi.utils.MessageUtil.formatOutputMessage;

import javax.validation.Valid;

import com.mediscreen.clientUi.constants.LogConstants;
import com.mediscreen.clientUi.constants.ViewNameConstants;
import com.mediscreen.clientUi.proxies.IPatientProxy;
import com.mediscreen.commons.dto.PatientDTO;
import com.mediscreen.commons.exceptions.PatientAlreadyExistException;
import com.mediscreen.commons.exceptions.PatientDoesNotExistException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@Api(value = "Actions for organizer profile")
public class OrganizerController {

    private final IPatientProxy patientProxy;

    @Autowired
    public OrganizerController(IPatientProxy patientProxy) {
        this.patientProxy = patientProxy;
    }

    @ApiOperation(value = "Show all registered patients")
    @GetMapping("/patient/list")
    public String showAllPatients(Model model) {

        log.debug(LogConstants.SHOW_ALL_PATIENTS_REQUEST_RECEIVED);

        model.addAttribute("patientDtoList", patientProxy.getAllPatients());

        return ViewNameConstants.SHOW_ALL_PATIENTS;
    }

    @ApiOperation(value = "Retrieve patient's information in an update form")
    @GetMapping("patient/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer patientId, Model model,
                                 RedirectAttributes redirectAttributes) {

        log.debug(LogConstants.SHOW_UPDATE_FORM_RECEIVED, patientId);

        try {
            model.addAttribute("patient", patientProxy.getPatientById(patientId));
            return ViewNameConstants.UPDATE_PATIENT;

        } catch (PatientDoesNotExistException patientDoesNotExistException) {
            log.error(patientDoesNotExistException.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage",
                                                 formatOutputMessage("patient.not.found", patientId.toString()));
            return "redirect:" + ViewNameConstants.SHOW_ALL_PATIENTS;
        }
    }

    @ApiOperation(value = "Update patient's information with data modified in the update form")
    @PostMapping("patient/update/{id}")
    public String updatePatient(@PathVariable("id") Integer patientId,
                                @ModelAttribute("patient") @Valid PatientDTO patientDTO,
                                BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        log.debug(LogConstants.UPDATE_PATIENT_RECEIVED, patientId);

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

        } catch (PatientDoesNotExistException | PatientAlreadyExistException patientException) {
            log.error(LogConstants.UPDATE_PATIENT_REQUEST_KO, patientId, patientException.getMessage());
            model.addAttribute("errorMessage",
                               formatOutputMessage("patient.update.ko",
                                                   patientId.toString()) + patientException.getMessage());
            return ViewNameConstants.UPDATE_PATIENT;

        } catch (Exception exception) {
            log.error(LogConstants.UPDATE_PATIENT_REQUEST_KO, patientId, exception.getMessage());
            model.addAttribute("errorMessage",
                               formatOutputMessage("patient.update.ko",
                                                   patientId.toString()) + exception.getMessage());
            return ViewNameConstants.UPDATE_PATIENT;
        }
    }

    @ApiOperation(value = "Show empty form to add a patient")
    @GetMapping("patient/add")
    public String showAddForm(Model model) {

        log.debug(LogConstants.SHOW_ADD_FORM_RECEIVED);

        model.addAttribute("patient", new PatientDTO());
        return ViewNameConstants.ADD_PATIENT;
    }

    @ApiOperation(value = "Add patient with data input of add form")
    @PostMapping("patient/add")
    public String addPatient(@ModelAttribute("patient") @Valid PatientDTO patientDTO,
                             BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        log.debug(LogConstants.ADD_PATIENT_RECEIVED);

        if (result.hasErrors()) {
            log.error(LogConstants.ADD_PATIENT_REQUEST_NOT_VALID);
            return ViewNameConstants.ADD_PATIENT;
        }

        try {
            patientDTO = patientProxy.addPatient(patientDTO);

            log.info(LogConstants.ADD_PATIENT_REQUEST_OK, patientDTO.getId());

            redirectAttributes.addFlashAttribute("infoMessage",
                                                 formatOutputMessage("patient.add.ok", patientDTO.getId().toString()));
            return "redirect:" + ViewNameConstants.SHOW_ALL_PATIENTS;

        } catch (PatientAlreadyExistException patientException) {
            log.error(LogConstants.ADD_PATIENT_REQUEST_KO, patientException.getMessage());
            model.addAttribute("errorMessage", "patient.add.ko" + patientException.getMessage()); //TODO
            return ViewNameConstants.ADD_PATIENT;

        } catch (Exception exception) {
            log.error(LogConstants.ADD_PATIENT_REQUEST_KO, exception.getMessage());
            model.addAttribute("errorMessage", "patient.add.ko" + exception.getMessage());
            return ViewNameConstants.ADD_PATIENT;
        }
    }
}

package com.mediscreen.clientUi.controller;

import static com.mediscreen.clientUi.utils.MessageUtil.formatOutputMessage;

import javax.validation.Valid;

import com.mediscreen.clientUi.constants.LogConstants;
import com.mediscreen.clientUi.constants.ViewNameConstants;
import com.mediscreen.clientUi.proxies.IPatientProxy;
import com.mediscreen.commons.dto.PatientDTO;
import com.mediscreen.commons.exceptions.PatientAlreadyExistException;
import com.mediscreen.commons.exceptions.PatientDoesNotExistException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
public class OrganizerController {

    private final static String DEFAULT_SORT_FIELD = "id";
    private final static String DEFAULT_SORT_DIRECTION = "asc";
    private final IPatientProxy patientProxy;

    @Autowired
    public OrganizerController(IPatientProxy patientProxy) {
        this.patientProxy = patientProxy;
    }

    @GetMapping("/organizer")
    public String showHomePageOrganizer(Model model) {

        log.debug(LogConstants.HOME_ORGANIZER_REQUEST_RECEIVED);
        return showAllPatientsByPage(model, 1, DEFAULT_SORT_FIELD, DEFAULT_SORT_DIRECTION, 5);
    }

    @GetMapping("/patient/list/{page}")
    public String showAllPatientsByPage(Model model,
                                        @PathVariable("page") int currentPage,
                                        @RequestParam("sortField") String sortField,
                                        @RequestParam("sortDir") String sortDir,
                                        @RequestParam("itemsPerPage") int itemsPerPage) {

        log.debug(LogConstants.SHOW_PATIENTS_PER_PAGE_REQUEST_RECEIVED, currentPage, sortField, sortDir);

        Page<PatientDTO> patientDTOPage = patientProxy.getAllPatientsByPage(currentPage, itemsPerPage, sortField,
                                                                            sortDir);

        model.addAttribute("patientDtoList", patientDTOPage.getContent());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", patientDTOPage.getTotalPages());
        model.addAttribute("totalItems", patientDTOPage.getTotalElements());
        model.addAttribute("itemsPerPage", itemsPerPage);

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        return ViewNameConstants.SHOW_ALL_PATIENTS;
    }

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
            return "redirect:" + ViewNameConstants.HOME_ORGANIZER;

        } catch (PatientDoesNotExistException | PatientAlreadyExistException patientException) {
            log.error(LogConstants.UPDATE_PATIENT_REQUEST_KO, patientId, patientException.getMessage());
            model.addAttribute("errorMessage",
                               formatOutputMessage("patient.update.ko",
                                                   patientId.toString()) + patientException.getMessage());
            return ViewNameConstants.UPDATE_PATIENT;

        }
    }

    @GetMapping("patient/add")
    public String showAddForm(Model model) {

        log.debug(LogConstants.SHOW_ADD_FORM_RECEIVED);

        model.addAttribute("patient", new PatientDTO());
        return ViewNameConstants.ADD_PATIENT;
    }

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
            return "redirect:" + ViewNameConstants.HOME_ORGANIZER;

        } catch (PatientAlreadyExistException patientException) {
            log.error(LogConstants.ADD_PATIENT_REQUEST_KO, patientException.getMessage());
            model.addAttribute("errorMessage", "patient.add.ko" + patientException.getMessage());
            return ViewNameConstants.ADD_PATIENT;

        }
    }

    @GetMapping("patient/delete/{id}")
    public String deletePatient(@PathVariable("id") Integer patientId, RedirectAttributes redirectAttributes) {

        log.debug(LogConstants.DELETE_REQUEST_RECEIVED, patientId);

        try {
            patientProxy.deletePatientById(patientId);
            log.info(LogConstants.DELETE_PATIENT_REQUEST_OK, patientId);
            redirectAttributes.addFlashAttribute("infoMessage",
                                                 formatOutputMessage("patient.delete.ok", patientId.toString()));

        } catch (PatientDoesNotExistException patientDoesNotExistException) {
            log.error(patientDoesNotExistException.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage",
                                                 formatOutputMessage("patient.not.found", patientId.toString()));
        }

        return "redirect:" + ViewNameConstants.HOME_ORGANIZER;
    }
}

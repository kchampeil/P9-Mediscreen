package com.mediscreen.clientUi.controller;

import static com.mediscreen.clientUi.utils.MessageUtil.formatOutputMessage;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import com.mediscreen.clientUi.constants.LogConstants;
import com.mediscreen.clientUi.constants.ViewNameConstants;
import com.mediscreen.clientUi.proxies.INoteProxy;
import com.mediscreen.clientUi.proxies.IPatientProxy;
import com.mediscreen.clientUi.proxies.IRiskAssessmentProxy;
import com.mediscreen.commons.dto.NoteDTO;
import com.mediscreen.commons.dto.PatientDTO;
import com.mediscreen.commons.dto.RiskAssessmentDTO;
import com.mediscreen.commons.exceptions.PatientDoesNotExistException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
public class RiskAssessmentController {

    private final IRiskAssessmentProxy riskAssessmentProxy;
    private final IPatientProxy patientProxy;
    private final INoteProxy noteProxy;

    @Autowired
    public RiskAssessmentController(IRiskAssessmentProxy riskAssessmentProxy,
                                    IPatientProxy patientProxy, INoteProxy noteProxy) {
        this.riskAssessmentProxy = riskAssessmentProxy;
        this.patientProxy = patientProxy;
        this.noteProxy = noteProxy;
    }

    @GetMapping("/assess/{id}")
    public String showRiskAssessmentResult(@PathVariable("id") Integer patientId, Model model,
                                           RedirectAttributes redirectAttributes) {

        log.debug(LogConstants.SHOW_RISK_ASSESSMENT_RESULT_RECEIVED, patientId);

        try {
            PatientDTO patientDTO = patientProxy.getPatientById(patientId);
            model.addAttribute("patient", patientDTO);
            model.addAttribute("age", Period.between(patientDTO.getBirthDate(), LocalDate.now()).getYears());

            List<NoteDTO> noteDTOList = noteProxy.getAllNotesForPatient(patientId);

            model.addAttribute("riskLevel",
                               riskAssessmentProxy.getDiabetesRiskForPatient(
                                   new RiskAssessmentDTO(patientDTO, noteDTOList)));
            return ViewNameConstants.RISK_ASSESSMENT_RESULT;

        } catch (PatientDoesNotExistException patientDoesNotExistException) {
            log.error(patientDoesNotExistException.getMessage() + patientId);
            redirectAttributes.addFlashAttribute("errorMessage",
                                                 formatOutputMessage("patient.not.found",
                                                                     patientId.toString()));
            return "redirect:" + ViewNameConstants.HOME_DOCTOR;
        }
    }
}

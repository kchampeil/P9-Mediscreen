package com.mediscreen.riskAssessment.service.contracts;

import java.util.List;

import com.mediscreen.commons.dto.NoteDTO;
import com.mediscreen.commons.dto.PatientDTO;
import com.mediscreen.riskAssessment.model.RiskLevel;

public interface IRiskAssessmentService {

    RiskLevel evaluateRisk(PatientDTO patientDTO, List<NoteDTO> noteDTOList);
}

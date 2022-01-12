package com.mediscreen.riskAssessment.service.contracts;

import com.mediscreen.commons.constants.RiskLevel;
import com.mediscreen.commons.dto.RiskAssessmentDTO;

public interface IRiskAssessmentService {

    RiskLevel evaluateRisk(RiskAssessmentDTO riskAssessmentDTO);
}

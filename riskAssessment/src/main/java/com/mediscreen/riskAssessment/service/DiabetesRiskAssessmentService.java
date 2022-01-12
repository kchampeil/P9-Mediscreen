package com.mediscreen.riskAssessment.service;

import static com.mediscreen.riskAssessment.constants.RiskConstants.FEMALE;
import static com.mediscreen.riskAssessment.constants.RiskConstants.MALE;
import static com.mediscreen.riskAssessment.constants.RiskConstants.MIN_RISK_FACTORS_FOR_BORDERLINE_ABOVE_AGE_LIMIT;
import static com.mediscreen.riskAssessment.constants.RiskConstants.MIN_RISK_FACTORS_FOR_EARLY_ONSET_ABOVE_AGE_LIMIT;
import static com.mediscreen.riskAssessment.constants.RiskConstants.MIN_RISK_FACTORS_FOR_EARLY_ONSET_FEMALE_UNDER_AGE_LIMIT;
import static com.mediscreen.riskAssessment.constants.RiskConstants.MIN_RISK_FACTORS_FOR_EARLY_ONSET_MALE_UNDER_AGE_LIMIT;
import static com.mediscreen.riskAssessment.constants.RiskConstants.MIN_RISK_FACTORS_FOR_IN_DANGER_ABOVE_AGE_LIMIT;
import static com.mediscreen.riskAssessment.constants.RiskConstants.MIN_RISK_FACTORS_FOR_IN_DANGER_FEMALE_UNDER_AGE_LIMIT;
import static com.mediscreen.riskAssessment.constants.RiskConstants.MIN_RISK_FACTORS_FOR_IN_DANGER_MALE_UNDER_AGE_LIMIT;
import static com.mediscreen.riskAssessment.constants.RiskConstants.NUMBER_RISK_FACTORS_FOR_NONE;
import static com.mediscreen.riskAssessment.constants.RiskConstants.RISK_AGE_LIMIT;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Locale;

import com.mediscreen.commons.dto.NoteDTO;
import com.mediscreen.commons.dto.PatientDTO;
import com.mediscreen.riskAssessment.model.DiabetesRiskFactors;
import com.mediscreen.riskAssessment.model.RiskLevel;
import com.mediscreen.riskAssessment.service.contracts.IRiskAssessmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Risk assessment for diabetes
 */

@Slf4j
@Service
public class DiabetesRiskAssessmentService implements IRiskAssessmentService {

    @Autowired
    public DiabetesRiskAssessmentService() {
    }

    /**
     * evaluate diabetes risk for one given patient and his medical notes
     *
     * @param patientDTO  patient we want to evaluate the diabetes risk
     * @param noteDTOList list of medical notes for the patient
     * @return risk level
     */
    @Override

    public RiskLevel evaluateRisk(PatientDTO patientDTO, List<NoteDTO> noteDTOList) {

        int age = Period.between(patientDTO.getBirthDate(), LocalDate.now()).getYears();

        int numberOfRiskFactors = countRiskFactors(noteDTOList);

        if (numberOfRiskFactors == NUMBER_RISK_FACTORS_FOR_NONE) {
            return RiskLevel.NONE;
        }

        if (age > RISK_AGE_LIMIT) {
            return calculateRiskForPatientAboveRiskAgeLimit(numberOfRiskFactors);
        }

        return calculateRiskForPatientUnderRiskAgeLimit(numberOfRiskFactors, patientDTO.getGender());
    }

    /**
     * count risk factors in patient's medical notes
     *
     * @param noteDTOList medical notes for the patient
     * @return the number of risk factors
     */
    private int countRiskFactors(List<NoteDTO> noteDTOList) {

        int numberOfRiskFactors = 0;

        for (DiabetesRiskFactors riskFactor : DiabetesRiskFactors.values()) {
            numberOfRiskFactors += noteDTOList.stream()
                                              .filter(noteDTO -> noteDTO.getNote()
                                                                        .toUpperCase(Locale.ROOT)
                                                                        .contains(riskFactor.getDescription()
                                                                                            .toUpperCase(Locale.ROOT)))
                                              .count();
        }

        return numberOfRiskFactors;
    }

    /**
     * calculate diabetes risk for patient above thirty years old
     *
     * @param numberOfRiskFactors found in patient's medical notes
     * @return risk level for diabetes
     */
    private RiskLevel calculateRiskForPatientAboveRiskAgeLimit(int numberOfRiskFactors) {
        if (numberOfRiskFactors >= MIN_RISK_FACTORS_FOR_EARLY_ONSET_ABOVE_AGE_LIMIT) { //8
            return RiskLevel.EARLY_ONSET;
        }
        if (numberOfRiskFactors >= MIN_RISK_FACTORS_FOR_IN_DANGER_ABOVE_AGE_LIMIT) { //6
            return RiskLevel.IN_DANGER;
        }
        if (numberOfRiskFactors >= MIN_RISK_FACTORS_FOR_BORDERLINE_ABOVE_AGE_LIMIT) { //2
            return RiskLevel.BORDERLINE;
        }
        return RiskLevel.NOT_APPLICABLE;
    }

    /**
     * calculate diabetes risk for patient under thirty years old (included)
     *
     * @param numberOfRiskFactors found in patient's medical notes
     * @param gender              of the patient
     * @return risk level for diabetes
     */
    private RiskLevel calculateRiskForPatientUnderRiskAgeLimit(int numberOfRiskFactors, String gender) {
        switch (gender) {
            case MALE:
                if (numberOfRiskFactors >= MIN_RISK_FACTORS_FOR_EARLY_ONSET_MALE_UNDER_AGE_LIMIT) { //5
                    return RiskLevel.EARLY_ONSET;
                }
                if (numberOfRiskFactors >= MIN_RISK_FACTORS_FOR_IN_DANGER_MALE_UNDER_AGE_LIMIT) { //3
                    return RiskLevel.IN_DANGER;
                }

            case FEMALE:
                if (numberOfRiskFactors >= MIN_RISK_FACTORS_FOR_EARLY_ONSET_FEMALE_UNDER_AGE_LIMIT) { //7
                    return RiskLevel.EARLY_ONSET;
                }
                if (numberOfRiskFactors >= MIN_RISK_FACTORS_FOR_IN_DANGER_FEMALE_UNDER_AGE_LIMIT) { //4
                    return RiskLevel.IN_DANGER;
                }

            default:
                return RiskLevel.NOT_APPLICABLE;
        }
    }
}

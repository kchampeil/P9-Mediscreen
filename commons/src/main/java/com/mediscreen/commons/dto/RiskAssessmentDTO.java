package com.mediscreen.commons.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RiskAssessmentDTO {

    private PatientDTO patientDTO;

    private List<NoteDTO> noteDTOList;

    public RiskAssessmentDTO(PatientDTO patientDTO, List<NoteDTO> noteDTOList) {
        this.patientDTO = patientDTO;
        this.noteDTOList = noteDTOList;
    }
}
